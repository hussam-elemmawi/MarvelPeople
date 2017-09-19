package com.hussamelemmawi.nanodegree.marvelpeople.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.hussamelemmawi.nanodegree.marvelpeople.R;
import com.hussamelemmawi.nanodegree.marvelpeople.data.source.local.PersistenceContract;

/**
 * Created by hussam_elemmawi on 25/11/16.
 */
public class MarvelPeopleWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new MarvelPeopleWidgetListProvider(this.getApplicationContext(), intent);
    }
}

class MarvelPeopleWidgetListProvider implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext = null;
    private int appWidgetId;
    Cursor mData;

    private static final String[] HERO_COLUMNS = {
            PersistenceContract.HeroEntry.COLUMN_HERO_ID,
            PersistenceContract.HeroEntry.COLUMN_NAME,
    };

    private static final int COLUMN_HERO_ID = 0;
    private static final int COLUMN_NAME = 1;
    private static final int COLUMN_COMICS_RETURNED = 2;
    private static final int COLUMN_SERIES_RETURNED = 3;
    private static final int COLUMN_STORIES_RETURNED = 4;
    private static final int COLUMN_EVENTS_RETURNED = 5;

    public MarvelPeopleWidgetListProvider(Context context, Intent intent) {
        this.mContext = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        // Retrieve data heroFrom db
        final long identityToken = Binder.clearCallingIdentity();
        mData = mContext.getContentResolver().query(
                PersistenceContract.HeroEntry.buildHeroesUri(),
                HERO_COLUMNS,
                null,
                null,
                null);
        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {
        if (mData != null) {
            mData.close();
            mData = null;
        }
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        if (position == AdapterView.INVALID_POSITION || mData == null ||
                !mData.moveToPosition(position)) {
            return null;
        }

        String heroId = mData.getString(COLUMN_HERO_ID);
        String name = mData.getString(COLUMN_NAME);
        int comics = mData.getInt(COLUMN_COMICS_RETURNED);
        int series = mData.getInt(COLUMN_SERIES_RETURNED);
        int stories = mData.getInt(COLUMN_STORIES_RETURNED);
        int events = mData.getInt(COLUMN_EVENTS_RETURNED);

        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(),
                R.layout.list_item_widget);


        String intoWidgetString = String.format(name + ", has" +
                Integer.toString(comics) + " comics, " +
                Integer.toString(series) + " series, " +
                Integer.toString(stories) + " stories, " +
                Integer.toString(events) + " events"
        );

        remoteViews.setTextViewText(R.id.text_hero_details, intoWidgetString);

        // Send the heroId into the pending template intent
        // to show correct hero
        final Intent fillinIntent = new Intent();
        fillinIntent.putExtra("heroId", heroId);
        remoteViews.setOnClickFillInIntent(R.id.widget_list_item, fillinIntent);

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return new RemoteViews(mContext.getPackageName(), R.layout.list_item_widget);
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
