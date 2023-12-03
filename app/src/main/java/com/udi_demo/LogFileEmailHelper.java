package com.udi_demo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * <p>
 * Add this to your app/build.gradle -> dependencies closure:
 * debugCompile 'com.github.tony19:logback-android-core:1.1.1-6'
 * debugCompile 'com.github.tony19:logback-android-classic:1.1.1-6'
 * <p>
 * Change email address LogFileEmailHelper#LOG_FILE_RECEIVER_EMAIL_ADDRESS
 * <p>
 * Add attached ‘logback.xml’ to ‘app/src/main/assets’ directory and make sure it’s included in the build.
 */
public class LogFileEmailHelper {
    // CHANGE THIS
    private static final String LOG_FILE_RECEIVER_EMAIL_ADDRESS = "udaykale35@gmail.com";
    private static final Logger LOGGER = LoggerFactory.getLogger(LogFileEmailHelper.class);

    public static void sendMailWithLogFiles(Activity activity, String textField, String subjectField) {
        String title = "Send Log files";

        Intent mailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        mailIntent.setType("message/rfc822");
        mailIntent.putExtra(Intent.EXTRA_EMAIL,
                new String[]{LOG_FILE_RECEIVER_EMAIL_ADDRESS});
        mailIntent.putExtra(Intent.EXTRA_SUBJECT, subjectField);
        mailIntent.putExtra(Intent.EXTRA_TEXT, textField);
        mailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        /* get all existing logfiles */
        List<File> logFiles = findLogFiles(activity);

        ArrayList<Uri> uris = new ArrayList<Uri>();
        for (File file : logFiles) {
            Uri u = FileProvider.getUriForFile(activity, activity.getString(R.string.file_provider_authority), file);
            uris.add(u);
        }
        mailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);

        LOGGER.debug("Dispatching mail to {} with {} log files", LOG_FILE_RECEIVER_EMAIL_ADDRESS, uris.size());

        activity.startActivityForResult(Intent.createChooser(mailIntent, title), 0 /* dummy */);
    }

    private static List<File> findLogFiles(final @NonNull Context context) {
        File[] matchedFiles = new File[0];
        List<File> movedFiles = new ArrayList<File>();

        File dataDir = context.getApplicationContext().getFilesDir();

        final Pattern p = Pattern.compile("^logback_log.*");

        if (dataDir != null && dataDir.exists()) {
            matchedFiles = dataDir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return p.matcher(name).matches();
                }
            });

            /* copy the files to external storage */
            for (File toMove : matchedFiles) {
                try {
                    File finalFile = moveFileToExternalStorage(context, toMove);
                    movedFiles.add(finalFile);
                } catch (Exception ex) {
                    // exception is already logged
                }

            }

        }

        return movedFiles;
    }

    private static File moveFileToExternalStorage(final Context context, final File baseFile) throws IOException {
        File extDir = context.getApplicationContext().getExternalFilesDir(null);
        File newFile = new File(extDir, baseFile.getName());
        FileChannel outputChannel = null;
        FileChannel inputChannel = null;
        try {
            outputChannel = new FileOutputStream(newFile).getChannel();
            inputChannel = new FileInputStream(baseFile).getChannel();
            inputChannel.transferTo(0, inputChannel.size(), outputChannel);
        } catch (Exception ex) {
            // could not copy this file, try next!
            LOGGER.debug("Could not copy logfile to external storage. Source: {} destination: {} exception: {}", baseFile.getAbsolutePath(), newFile.getAbsolutePath(),
                    ex);
        } finally {
            if (inputChannel != null) {
                inputChannel.close();
            }
            if (outputChannel != null) {
                outputChannel.close();
            }
        }
        return newFile;
    }
}