"""Prépare le projet Android : Marama Iti devient un écran d'accueil possible."""
import pathlib, re, shutil

root = pathlib.Path(__file__).resolve().parent.parent
android = root / "android" / "app" / "src" / "main"
java = android / "java" / "pf" / "maramaiti" / "app"
java.mkdir(parents=True, exist_ok=True)
for f in (root / "native").glob("*.java"):
    shutil.copy(f, java / f.name)

manifest = android / "AndroidManifest.xml"
m = manifest.read_text(encoding="utf-8")

# 1. Se déclarer comme écran d'accueil (HOME)
home = """
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.HOME" />
                <category android:name="android.intent.category.DEFAULT" />
            </intent-filter>
"""
if "android.intent.category.HOME" not in m:
    i = m.index("</intent-filter>") + len("</intent-filter>")
    m = m[:i] + home + m[i:]

# 2. Une seule instance de l'écran d'accueil
if 'android:launchMode="singleTask"' not in m:
    m = m.replace('android:name=".MainActivity"', 'android:name=".MainActivity"\n            android:launchMode="singleTask"', 1)

# 3. Pouvoir voir les applications installées (Android 11+)
if "<queries>" not in m:
    q = """    <uses-permission android:name="com.android.alarm.permission.SET_ALARM" />
    <uses-permission android:name="android.permission.RECORD_AUDIO" />
    <uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS" />

    <queries>
        <intent>
            <action android:name="android.intent.action.MAIN" />
            <category android:name="android.intent.category.LAUNCHER" />
        </intent>
        <intent><action android:name="android.speech.action.RECOGNIZE_SPEECH" /></intent>
        <intent><action android:name="android.intent.action.TTS_SERVICE" /></intent>
        <intent><action android:name="android.intent.action.SET_ALARM" /></intent>
        <intent><action android:name="android.support.customtabs.action.CustomTabsService" /></intent>
        <intent>
            <action android:name="android.intent.action.VIEW" />
            <data android:scheme="https" />
        </intent>
    </queries>

"""
    m = m.replace("<application", q + "    <application", 1)

manifest.write_text(m, encoding="utf-8")
print("Manifeste Android prêt.")

# 4. Numéro de version qui augmente à chaque construction
#    (Android n'installe une mise à jour que si ce numéro est plus grand)
import os
gradle = root / "android" / "app" / "build.gradle"
if gradle.exists():
    g = gradle.read_text(encoding="utf-8")
    code = os.environ.get("VERSION_CODE")
    name = os.environ.get("VERSION_NAME")
    if code:
        g = re.sub(r"versionCode\s+\d+", f"versionCode {int(code)}", g)
    if name:
        g = re.sub(r'versionName\s+"[^"]*"', f'versionName "{name}"', g)
    gradle.write_text(g, encoding="utf-8")
    print(f"Version Android : {name or '?'} ({code or '?'})")
