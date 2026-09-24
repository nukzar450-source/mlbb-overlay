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
        setTitle(getString(R.string.app_name));
        setContentView(createContentView());
    }

    private View createContentView() {
        ScrollView scrollView = new ScrollView(this);
        scrollView.setFillViewport(true);
        scrollView.setBackgroundColor(Color.rgb(12, 16, 22));

        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setGravity(Gravity.CENTER_HORIZONTAL);
        content.setPadding(dp(24), dp(24), dp(24), dp(24));

        TextView title = new TextView(this);
        title.setText(R.string.loader_title);
        title.setTextColor(Color.WHITE);
        title.setTextSize(28);
        title.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        title.setGravity(Gravity.CENTER);
        content.addView(title, widthParams(dp(320), dp(56)));

        TextView subtitle = new TextView(this);
        subtitle.setText(R.string.loader_subtitle);
        subtitle.setTextColor(Color.rgb(180, 190, 205));
        subtitle.setTextSize(14);
        subtitle.setGravity(Gravity.CENTER);
        content.addView(subtitle, widthParams(dp(320), dp(44)));

        statusText = new TextView(this);
        statusText.setText(R.string.status_ready);
        statusText.setTextColor(Color.rgb(180, 190, 205));
        statusText.setTextSize(14);
        statusText.setGravity(Gravity.CENTER);
        content.addView(statusText, widthParams(dp(320), dp(36)));

        Button launchButton = createButton(R.string.action_launch, 54);
        launchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchOverlay();
            }
        });
        content.addView(launchButton, widthParams(dp(320), dp(62)));

        LinearLayout actions = new LinearLayout(this);
        actions.setOrientation(LinearLayout.HORIZONTAL);
        actions.setGravity(Gravity.CENTER);

        Button startButton = createButton(R.string.action_start, 46);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startOverlay();
            }
        });

        Button stopButton = createButton(R.string.action_stop, 46);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopOverlay();
            }
        });

        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                0, dp(52), 1.0f);
        buttonParams.setMargins(dp(6), dp(12), dp(6), 0);
        actions.addView(startButton, buttonParams);
        actions.addView(stopButton, new LinearLayout.LayoutParams(buttonParams));
        content.addView(actions, widthParams(dp(320), dp(72)));

        TextView hint = new TextView(this);
        hint.setText(R.string.permissions_hint);
        hint.setTextColor(Color.rgb(150, 160, 172));
        hint.setTextSize(12);
        hint.setGravity(Gravity.CENTER);
        content.addView(hint, widthParams(dp(320), dp(60)));

        scrollView.addView(content);
        return scrollView;
    }

    private Button createButton(int textRes, int minHeightDp) {
        Button button = new Button(this);
        button.setText(textRes);
        button.setTextColor(Color.WHITE);
        button.setTextSize(16);
        button.setAllCaps(false);
        button.setMinHeight(dp(minHeightDp));
        button.setGravity(Gravity.CENTER);
        button.setBackgroundColor(Color.rgb(32, 52, 74));
        return button;
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
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M
                || Settings.canDrawOverlays(this);
    }

    private void openOverlaySettings() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        }
    }

    private void setStatus(int textRes) {
        if (statusText != null) {
            statusText.setText(textRes);
        }
    }

    private LinearLayout.LayoutParams widthParams(int width, int height) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
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
            if (hasOverlayPermission()) {
                statusText.setText(R.string.status_ready);
            } else {
                statusText.setText(R.string.status_permission_required);
            }
        }
    }
}
