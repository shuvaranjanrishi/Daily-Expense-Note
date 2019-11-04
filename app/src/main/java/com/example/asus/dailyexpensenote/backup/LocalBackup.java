package com.example.asus.dailyexpensenote.backup;

import android.app.AlertDialog;
import android.os.Environment;
import android.text.InputType;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.example.asus.dailyexpensenote.R;
import com.example.asus.dailyexpensenote.activity.MainActivity;
import com.example.asus.dailyexpensenote.database.MyDBHelper;
import com.example.asus.dailyexpensenote.permission.Permissions;

import java.io.File;

public class LocalBackup {

    private MainActivity activity;

    public LocalBackup(MainActivity activity) {
        this.activity = activity;
    }

    //ask to the user a name for the backup and perform it. The backup will be saved to a custom folder.
    public void performBackup(final MyDBHelper myDBHelper, final String outFileName) {

        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)){

            Permissions.verifyStoragePermissions(activity);

            File folder = new File(Environment.getExternalStorageDirectory() + File.separator + activity.getResources().getString(R.string.app_name));

            boolean success = true;

            if (!folder.exists())
                success = folder.mkdirs();

            if (success) {

                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Give your backup file name");
                final EditText input = new EditText(activity);
                input.setHint("give name of backup file");
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("Save", (dialog, which) -> {
                    String fileName = input.getText().toString();
                    String out = outFileName + fileName + ".db";
                    myDBHelper.backup(out);
                });

                builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

                builder.show();
            } else {
                Toast.makeText(activity, "Unable to create directory. Retry...", Toast.LENGTH_SHORT).show();
            }

        }else {
            Toast.makeText(activity, "SD Card is not available", Toast.LENGTH_SHORT).show();
        }
    }

    //ask to the user what backup to restore
    public void performRestore(final MyDBHelper myDBHelper) {

        Permissions.verifyStoragePermissions(activity);

        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + activity.getResources().getString(R.string.app_name));
        if (folder.exists()) {

            final File[] files = folder.listFiles();

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(activity, android.R.layout.select_dialog_item);
            for (File file : files)
                arrayAdapter.add(file.getName());

            AlertDialog.Builder builderSingle = new AlertDialog.Builder(activity);
            builderSingle.setTitle("Select Your Backup file to Restore");

            builderSingle.setNegativeButton("cancel", (dialog, which) -> dialog.dismiss());

            builderSingle.setAdapter(
                    arrayAdapter,
                    (dialog, which) -> {
                        try {
                            myDBHelper.importDB(files[which].getPath());
                        } catch (Exception e) {
                            Toast.makeText(activity, "Unable to restore. Retry", Toast.LENGTH_SHORT).show();
                        }
                    });
            builderSingle.show();
        } else
            Toast.makeText(activity, "Backup folder not present.\nDo a backup before a restore!", Toast.LENGTH_SHORT).show();
    }

}
