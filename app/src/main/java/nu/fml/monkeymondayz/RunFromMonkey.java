package nu.fml.monkeymondayz;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Hawry on 2015-05-19.
 */
public class RunFromMonkey extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent monkey = new Intent(context, MonkeyFinderService.class);
        context.stopService(monkey);
        context.startService(monkey);

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(50);

        System.out.println("Cancelled and hopefully restarted the monkeyfinder");
    }
}
