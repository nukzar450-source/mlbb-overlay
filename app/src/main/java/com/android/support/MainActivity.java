package com.android.support;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends Activity {
    private TextView statusText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(createContentView());
    }

    private View createContentView() {
        ScrollView root = new ScrollView(this);
        root.setFillViewport(true);
        root.setBackgroundColor(Color.rgb(12, 16, 22));

        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setGravity(Gravity.CENTER_HORIZONTAL);
        content.setPadding(dp(24), dp(24), dp(24), dp(24));

        TextView title = new TextView(this);
        title.setText(R.string.loader_title);
        title.setTextColor(Color.WHITE);
        title.setTextSize(28);
        title.setTypeface(Typeface.DEFAULT_BOLD);
        title.setGravity(Gravity.CENTER);
        content.addView(title, wrapCenter(dp(320), dp(58)));

        TextView subtitle = new TextView(this);
        subtitle.setText(R.string.loader_subtitle);
        subtitle.setTextColor(Color.rgb(180, 190, 205));
        subtitle.setTextSize(14);
        subtitle.setGravity(Gravity.CENTER);
        content.addView(subtitle, wrapCenter(dp(320), dp(42)));

        statusText = new TextView(this);
        statusText.setText(hasOverlayPermission() ? R.string.status_ready : R.string.status_permission_required);
        statusText.setTextColor(Color.rgb(180, 190, 205));
        statusText.setTextSize(14);
        statusText.setGravity(Gravity.CENTER);
        content.addView(statusText, wrapCenter(dp(320), dp(36)));

        Button launchButton = new Button(this);
        launchButton.setText(R.string.action_launch);
        launchButton.setTextColor(Color.WHITE);
        launchButton.setTextSize(18);
        launchButton.setAllCaps(false);
        launchButton.setBackgroundColor(Color.rgb(26, 104, 238));
        launchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchOverlay();
            }
        });
        content.addView(launchButton, wrapCenter(dp(320), dp(62)));

        LinearLayout controls = new LinearLayout(this);
        controls.setOrientation(LinearLayout.HORIZONTAL);
        controls.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams controlParams = new LinearLayout.LayoutParams(
                0, dp(52), 1.0f
        );
        controlParams.setMargins(dp(6), dp(12), dp(6), 0);

        Button startButton = new Button(this);
        startButton.setText(R.string.action_start);
        startButton.setTextColor(Color.WHITE);
        startButton.setTextSize(16);
        startButton.setAllCaps(false);
        startButton.setBackgroundColor(Color.rgb(23, 120, 73));
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startOverlay();
            }
        });

        Button stopButton = new Button(this);
        stopButton.setText(R.string.action_stop);
        stopButton.setTextColor(Color.WHITE);
        stopButton.setTextSize(16);
        stopButton.setAllCaps(false);
        stopButton.setBackgroundColor(Color.rgb(162, 53, 53));
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopOverlay();
            }
        });

        controls.addView(startButton, controlParams);
        controls.addView(stopButton, new LinearLayout.LayoutParams(controlParams));
        content.addView(controls, wrapCenter(dp(320), dp(76)));

        TextView hint = new TextView(this);
        hint.setText(R.string.permissions_hint);
        hint.setTextColor(Color.rgb(150, 160, 172));
        hint.setTextSize(12);
        hint.setGravity(Gravity.CENTER);
        content.addView(hint, wrapCenter(dp(320), dp(60)));

        root.addView(content);
        return root;
    }

    private void launchOverlay() {
        if (!hasOverlayPermission()) {
            openOverlaySettings();
            setStatus(R.string.status_permission_required);
            return;
        }
        startOverlay();
    }

    private void startOverlay() {
        if (!hasOverlayPermission()) {
            openOverlaySettings();
            setStatus(R.string.status_permission_required);
            return;
        }

        try {
            startService(new Intent(this, Launcher.class));
            setStatus(R.string.status_running);
        } catch (RuntimeException ignored) {
            setStatus(R.string.status_start_failed);
        }
    }

    private void stopOverlay() {
        stopService(new Intent(this, Launcher.class));
        setStatus(R.string.status_stopped);
    }

    private boolean hasOverlayPermission() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(this);
    }

    private void openOverlaySettings() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName())
            );
            startActivity(intent);
        }
    }

    private void setStatus(int textResId) {
        if (statusText != null) {
            statusText.setText(textResId);
        }
    }

    private LinearLayout.LayoutParams wrapCenter(int widthPx, int heightPx) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(widthPx, heightPx);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        return params;
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (statusText != null) {
            statusText.setText(hasOverlayPermission()
                    ? R.string.status_ready
                    : R.string.status_permission_required);
        }
    }
}
