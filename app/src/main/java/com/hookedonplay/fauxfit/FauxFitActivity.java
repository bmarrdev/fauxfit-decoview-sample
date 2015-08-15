/*
 * Copyright (C) 2015 Brent Marriott
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hookedonplay.fauxfit;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AnticipateInterpolator;
import android.widget.TextView;

import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.DecoDrawEffect;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;

/**
 * Sample to create an animated arc based chart in the style used by Google Fit
 * <p/>
 * This is achieved using the open source DecoView library.
 *
 * @see <a href="https://github.com/bmarrdev/android-DecoView-charting">DecoView on GitHub</a>
 */
public class FauxFitActivity extends AppCompatActivity {

    /**
     * DecoView animated arc based chart
     */
    private DecoView mDecoView;

    /**
     * Data series index used for controlling animation of {@link DecoView}. These are set when
     * the data series is created then used in {@link #createEvents} to specify what series to
     * apply a given event to
     */
    private int mBackIndex;
    private int mSeries1Index;
    private int mSeries2Index;
    private int mSeries3Index;

    /**
     * Maximum value for each data series in the {@link DecoView}. This can be different for each
     * data series, in this example we are applying the same all data series
     */
    private final float mSeriesMax = 50f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faux_fit);

        mDecoView = (DecoView) findViewById(R.id.dynamicArcView);

        // Create required data series on the DecoView
        createBackSeries();
        createDataSeries1();
        createDataSeries2();
        createDataSeries3();

        // Setup events to be fired on a schedule
        createEvents();
    }

    private void createBackSeries() {
        SeriesItem seriesItem = new SeriesItem.Builder(Color.parseColor("#FFE2E2E2"))
                .setRange(0, mSeriesMax, 0)
                .setInitialVisibility(true)
                .build();

        mBackIndex = mDecoView.addSeries(seriesItem);
    }

    private void createDataSeries1() {
        final SeriesItem seriesItem = new SeriesItem.Builder(Color.parseColor("#FFFF8800"))
                .setRange(0, mSeriesMax, 0)
                .setInitialVisibility(false)
                .build();

        final TextView textPercentage = (TextView) findViewById(R.id.textPercentage);
        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                float percentFilled = ((currentPosition - seriesItem.getMinValue()) / (seriesItem.getMaxValue() - seriesItem.getMinValue()));
                textPercentage.setText(String.format("%.0f%%", percentFilled * 100f));
            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });


        final TextView textToGo = (TextView) findViewById(R.id.textRemaining);
        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                textToGo.setText(String.format("%.1f Km to goal", seriesItem.getMaxValue() - currentPosition));

            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });

        final TextView textActivity1 = (TextView) findViewById(R.id.textActivity1);
        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                textActivity1.setText(String.format("%.0f Km", currentPosition));
            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });

        mSeries1Index = mDecoView.addSeries(seriesItem);
    }

    private void createDataSeries2() {
        final SeriesItem seriesItem = new SeriesItem.Builder(Color.parseColor("#FFFF4444"))
                .setRange(0, mSeriesMax, 0)
                .setInitialVisibility(false)
                .build();

        final TextView textActivity2 = (TextView) findViewById(R.id.textActivity2);

        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                textActivity2.setText(String.format("%.0f Km", currentPosition));
            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });

        mSeries2Index = mDecoView.addSeries(seriesItem);
    }

    private void createDataSeries3() {
        final SeriesItem seriesItem = new SeriesItem.Builder(Color.parseColor("#FF6699FF"))
                .setRange(0, mSeriesMax, 0)
                .setInitialVisibility(false)
                .build();

        final TextView textActivity3 = (TextView) findViewById(R.id.textActivity3);

        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                textActivity3.setText(String.format("%.2f Km", currentPosition));
            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });

        mSeries3Index = mDecoView.addSeries(seriesItem);
    }

    private void createEvents() {
        mDecoView.executeReset();

        mDecoView.addEvent(new DecoEvent.Builder(mSeriesMax)
                .setIndex(mBackIndex)
                .setDuration(3000)
                .setDelay(100)
                .build());

        mDecoView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_OUT)
                .setIndex(mSeries1Index)
                .setDuration(2000)
                .setDelay(1250)
                .build());

        mDecoView.addEvent(new DecoEvent.Builder(42.4f)
                .setIndex(mSeries1Index)
                .setDelay(3250)
                .build());

        mDecoView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_OUT)
                .setIndex(mSeries2Index)
                .setDuration(1000)
                .setEffectRotations(1)
                .setDelay(7000)
                .build());

        mDecoView.addEvent(new DecoEvent.Builder(16.3f)
                .setIndex(mSeries2Index)
                .setDelay(8500)
                .build());

        mDecoView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_OUT)
                .setIndex(mSeries3Index)
                .setDuration(1000)
                .setEffectRotations(1)
                .setDelay(12500)
                .build());

        mDecoView.addEvent(new DecoEvent.Builder(4.36f).setIndex(mSeries3Index).setDelay(14000).build());

        mDecoView.addEvent(new DecoEvent.Builder(0).setIndex(mSeries3Index).setDelay(18000).build());

        mDecoView.addEvent(new DecoEvent.Builder(0).setIndex(mSeries2Index).setDelay(18000).build());

        mDecoView.addEvent(new DecoEvent.Builder(0)
                .setIndex(mSeries1Index)
                .setDelay(20000)
                .setDuration(1000)
                .setInterpolator(new AnticipateInterpolator())
                .setListener(new DecoEvent.ExecuteEventListener() {
                    @Override
                    public void onEventStart(DecoEvent decoEvent) {

                    }

                    @Override
                    public void onEventEnd(DecoEvent decoEvent) {
                        resetText();
                    }
                })
                .build());

        mDecoView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_EXPLODE)
                .setIndex(mSeries1Index)
                .setDelay(21000)
                .setDuration(3000)
                .setDisplayText("GOAL!")
                .setListener(new DecoEvent.ExecuteEventListener() {
                    @Override
                    public void onEventStart(DecoEvent decoEvent) {

                    }

                    @Override
                    public void onEventEnd(DecoEvent decoEvent) {
                        createEvents();
                    }
                })
                .build());

        resetText();
    }

    private void resetText() {
        ((TextView) findViewById(R.id.textActivity1)).setText("");
        ((TextView) findViewById(R.id.textActivity2)).setText("");
        ((TextView) findViewById(R.id.textActivity3)).setText("");
        ((TextView) findViewById(R.id.textPercentage)).setText("");
        ((TextView) findViewById(R.id.textRemaining)).setText("");
    }
}
