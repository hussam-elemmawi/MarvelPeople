package com.hussamelemmawi.nanodegree.marvelpeople.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;

import com.hussamelemmawi.nanodegree.marvelpeople.Filters;
import com.hussamelemmawi.nanodegree.marvelpeople.MarvelActivity;
import com.hussamelemmawi.nanodegree.marvelpeople.MarvelDetailsActivity;
import com.hussamelemmawi.nanodegree.marvelpeople.R;
import com.hussamelemmawi.nanodegree.marvelpeople.heroes.HeroesPresenter;


/**
 * Created by hussam_elemmawi on 25/11/16.
 */
public class MarvelPeopleWidgetProvider extends AppWidgetProvider {

  @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    // update each of the widgets with the remote adapter
    for (int i = 0; i < appWidgetIds.length; ++i) {

      // Here we setup the intent which points to the StackViewService which will
      // provide the views for this collection.
      Intent intent = new Intent(context, MarvelPeopleWidgetService.class);
      intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
      // When intents are compared, the extras are ignored, so we need to embed the extras
      // into the data so that the extras will not be ignored.
      intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
      RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_initial_layout);

      // Set up the collection
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
        setRemoteAdapter(context, views);
      } else {
        setRemoteAdapterV11(context, views);
      }

      views.setRemoteAdapter(appWidgetIds[i], R.id.widget_list, intent);

      // For the main widget frame clicks, opens the MyStocksActivity
      Intent myStocksActivityIntent = new Intent(context, MarvelActivity.class);
      PendingIntent myStocksActivityPendingIntent = PendingIntent.getActivity(context, 0,
        myStocksActivityIntent, 0);
      views.setOnClickPendingIntent(R.id.widget_header, myStocksActivityPendingIntent);

      Intent individualActionIntent = new Intent(context, MarvelDetailsActivity.class);
      intent.putExtra(context.getString(R.string.fragment_key), Filters.MarvelMode.HEROES);

      // For the individual view heroFrom the heroes list clicks
      PendingIntent individualActionPendingIntent = TaskStackBuilder.create(context)
        .addNextIntentWithParentStack(individualActionIntent)
        .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
      views.setPendingIntentTemplate(R.id.widget_list, individualActionPendingIntent);

      // The empty view is displayed when the collection has no items. It should be a sibling
      // of the collection view.
      views.setEmptyView(R.id.widget_list, R.id.empty_view);

      appWidgetManager.updateAppWidget(appWidgetIds[i], views);
    }
    super.onUpdate(context, appWidgetManager, appWidgetIds);
  }

  @Override
  public void onReceive(Context context, Intent intent) {
    super.onReceive(context, intent);
    if (HeroesPresenter.ACTION_DATA_UPDATE.equals(intent.getAction())) {
      AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
      int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
        new ComponentName(context, getClass()));
      appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list);
    }
  }

  /**
   * Sets the remote adapter used to fill in the list items
   *
   * @param views RemoteViews to set the RemoteAdapter
   */
  @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
  private void setRemoteAdapter(Context context, @NonNull final RemoteViews views) {
    views.setRemoteAdapter(R.id.widget_list,
      new Intent(context, MarvelPeopleWidgetService.class));
  }

  /**
   * Sets the remote adapter used to fill in the list items
   *
   * @param views RemoteViews to set the RemoteAdapter
   */
  @SuppressWarnings("deprecation")
  private void setRemoteAdapterV11(Context context, @NonNull final RemoteViews views) {
    views.setRemoteAdapter(0, R.id.widget_list,
      new Intent(context, MarvelPeopleWidgetService.class));
  }
}