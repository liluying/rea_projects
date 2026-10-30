package com.rea.wajibao.replica;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;
import com.rea.wajibao.replica.core.Design;
import com.rea.wajibao.replica.core.Guidance;
import com.rea.wajibao.replica.core.PoseSolver;
import java.util.Locale;

final class ProfileView extends View {
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Design design;
    private PoseSolver.Pose pose;
    ProfileView(Context context) { super(context); setLayerType(View.LAYER_TYPE_SOFTWARE, null); }
    void clear() { design = null; pose = null; setContentDescription("计算不可用"); invalidate(); }
    void update(Design d, PoseSolver.Pose p) {
        design = d; pose = p;
        setContentDescription(String.format(Locale.CHINA, "%s横断面，斗齿高程 %.3f 米，设计高程 %.3f 米",
                d.label(), p.elevation, d.elevationAt(p.lateral))); invalidate();
    }
    @Override protected void onDraw(Canvas c) {
        super.onDraw(c); if (design == null || pose == null) return;
        float density = getResources().getDisplayMetrics().density;
        float l = 55 * density, r = getWidth() - 20 * density;
        float top = 30 * density, bottom = getHeight() - 35 * density;
        double span = design.mode == Design.Mode.TRENCH
                ? Math.max(5, design.width / 2 + design.sideRatio * design.depth + 1) : 5;
        double low = Math.min(design.surface - design.depth, pose.elevation) - .6;
        double high = Math.max(design.surface, pose.elevation) + .6;
        double range = high - low;
        c.drawColor(Color.rgb(16, 44, 49)); paint.setTextSize(10 * density);
        for (int i = 0; i <= 4; i++) {
            float y = top + (bottom - top) * i / 4;
            paint.setColor(Color.rgb(45, 71, 75)); paint.setStrokeWidth(density);
            c.drawLine(l, y, r, y, paint);
            paint.setColor(Color.rgb(159, 185, 185));
            c.drawText(String.format(Locale.ROOT, "%.1f", high - range * i / 4), 5 * density, y + 4 * density, paint);
        }
        Path line = new Path();
        for (int i = 0; i <= 320; i++) {
            double offset = -span + 2 * span * i / 320;
            float x = l + (r - l) * i / 320;
            float y = (float) (bottom - (design.elevationAt(offset) - low) / range * (bottom - top));
            if (i == 0) line.moveTo(x, y); else line.lineTo(x, y);
        }
        paint.setStyle(Paint.Style.STROKE); paint.setColor(Color.rgb(89, 220, 181));
        paint.setStrokeWidth(3 * density); c.drawPath(line, paint); paint.setStyle(Paint.Style.FILL);
        float px = (float) (l + (pose.lateral + span) / (2 * span) * (r - l));
        float py = (float) (bottom - (pose.elevation - low) / range * (bottom - top));
        float target = (float) (bottom - (design.elevationAt(pose.lateral) - low) / range * (bottom - top));
        paint.setColor(Color.rgb(246, 190, 92)); paint.setStrokeWidth(2 * density);
        c.drawLine(px, py, px, target, paint); c.drawCircle(px, py, 6 * density, paint);
        paint.setTextSize(11 * density); c.drawText("斗齿", Math.min(px + 10 * density, r - 30 * density), py - 10 * density, paint);
        paint.setColor(Color.rgb(181, 208, 207));
        c.drawText("高程 m", 8 * density, 16 * density, paint);
        c.drawText("横断面  /  横向偏移 m", l, getHeight() - 7 * density, paint);
        c.drawText(String.format(Locale.ROOT, "−%.1f", span), l, bottom + 17 * density, paint);
        c.drawText("0", (l + r) / 2, bottom + 17 * density, paint);
        c.drawText(String.format(Locale.ROOT, "+%.1f", span), r - 28 * density, bottom + 17 * density, paint);
    }
}
