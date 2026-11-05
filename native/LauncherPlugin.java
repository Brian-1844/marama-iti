package pf.maramaiti.app;

import android.app.Activity;
import android.content.Intent;
import android.provider.AlarmClock;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import androidx.activity.result.ActivityResult;
import com.getcapacitor.annotation.ActivityCallback;
import java.util.ArrayList;
import java.util.Locale;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.util.Base64;

import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Module Marama Iti : liste et ouvre les applications installées,
 * et permet de devenir l'écran d'accueil du téléphone.
 */
@CapacitorPlugin(name = "Launcher")
public class LauncherPlugin extends Plugin {

    private TextToSpeech tts;

    @Override
    public void load() {
        tts = new TextToSpeech(getContext(), status -> {
            if (status == TextToSpeech.SUCCESS && tts != null) tts.setLanguage(Locale.FRANCE);
        });
    }

    @Override
    protected void handleOnDestroy() {
        if (tts != null) { tts.shutdown(); tts = null; }
    }

    /** Lecture à voix haute (la WebView Android n'a pas de synthèse vocale). */
    @PluginMethod
    public void speak(PluginCall call) {
        if (tts == null) { call.reject("synthèse vocale indisponible"); return; }
        tts.setSpeechRate(call.getFloat("rate", 0.95f));
        tts.speak(call.getString("text", ""), TextToSpeech.QUEUE_FLUSH, null, "marama");
        call.resolve();
    }

    @PluginMethod
    public void stopSpeaking(PluginCall call) {
        if (tts != null) tts.stop();
        call.resolve();
    }

    /** Commande vocale : ouvre la reconnaissance vocale d'Android et renvoie le texte. */
    @PluginMethod
    public void listen(PluginCall call) {
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, call.getString("lang", "fr-FR"));
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, call.getString("prompt", "Parlez maintenant"));
        i.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, (int) 3);
        try {
            startActivityForResult(call, i, "listenResult");
        } catch (Exception e) {
            call.reject("reconnaissance vocale indisponible");
        }
    }

    @ActivityCallback
    private void listenResult(PluginCall call, ActivityResult result) {
        if (call == null) return;
        JSObject r = new JSObject();
        JSArray all = new JSArray();
        String text = "";
        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
            ArrayList<String> m = result.getData().getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (m != null && !m.isEmpty()) { text = m.get(0); for (String s : m) all.put(s); }
        }
        r.put("text", text);
        r.put("all", all);
        call.resolve(r);
    }

    /** Réveil : règle une alarme dans l'application Horloge du téléphone. */
    @PluginMethod
    public void setAlarm(PluginCall call) {
        Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
        i.putExtra(AlarmClock.EXTRA_HOUR, (int) call.getInt("hour", 7));
        i.putExtra(AlarmClock.EXTRA_MINUTES, (int) call.getInt("minute", 0));
        i.putExtra(AlarmClock.EXTRA_MESSAGE, call.getString("message", "Marama Iti"));
        i.putExtra(AlarmClock.EXTRA_SKIP_UI, false);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try { getContext().startActivity(i); call.resolve(); }
        catch (Exception e) { call.reject("application Horloge introuvable"); }
    }

    @PluginMethod
    public void showAlarms(PluginCall call) {
        Intent i = new Intent(AlarmClock.ACTION_SHOW_ALARMS);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try { getContext().startActivity(i); call.resolve(); }
        catch (Exception e) { call.reject("application Horloge introuvable"); }
    }

    @PluginMethod
    public void getApps(PluginCall call) {
        PackageManager pm = getContext().getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> list = pm.queryIntentActivities(intent, 0);
        String self = getContext().getPackageName();
        JSArray apps = new JSArray();
        for (ResolveInfo ri : list) {
            String pkg = ri.activityInfo.packageName;
            if (pkg.equals(self)) continue;
            JSObject o = new JSObject();
            o.put("name", ri.loadLabel(pm).toString());
            o.put("package", pkg);
            o.put("icon", iconToDataUrl(ri.loadIcon(pm)));
            apps.put(o);
        }
        JSObject ret = new JSObject();
        ret.put("apps", apps);
        call.resolve(ret);
    }

    @PluginMethod
    public void launch(PluginCall call) {
        String pkg = call.getString("package");
        if (pkg == null) { call.reject("package manquant"); return; }
        Intent li = getContext().getPackageManager().getLaunchIntentForPackage(pkg);
        if (li == null) { call.reject("application introuvable"); return; }
        li.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(li);
        call.resolve();
    }

    @PluginMethod
    public void isDefault(PluginCall call) {
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.addCategory(Intent.CATEGORY_HOME);
        ResolveInfo r = getContext().getPackageManager().resolveActivity(home, PackageManager.MATCH_DEFAULT_ONLY);
        boolean def = r != null && r.activityInfo != null
                && getContext().getPackageName().equals(r.activityInfo.packageName);
        JSObject ret = new JSObject();
        ret.put("value", def);
        call.resolve(ret);
    }

    @PluginMethod
    public void chooseDefault(PluginCall call) {
        try {
            Intent s = new Intent(Settings.ACTION_HOME_SETTINGS);
            s.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(s);
        } catch (Exception e) {
            Intent s = new Intent(Settings.ACTION_SETTINGS);
            s.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(s);
        }
        call.resolve();
    }

    @PluginMethod
    public void openSettings(PluginCall call) {
        Intent s = new Intent(Settings.ACTION_SETTINGS);
        s.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(s);
        call.resolve();
    }

    private String iconToDataUrl(Drawable d) {
        try {
            int size = 96;
            Bitmap bmp = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(bmp);
            d.setBounds(0, 0, size, size);
            d.draw(c);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
            return "data:image/png;base64," + Base64.encodeToString(out.toByteArray(), Base64.NO_WRAP);
        } catch (Exception e) {
            return "";
        }
    }
}
