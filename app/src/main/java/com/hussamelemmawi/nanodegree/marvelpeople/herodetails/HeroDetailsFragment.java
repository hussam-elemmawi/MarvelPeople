package com.hussamelemmawi.nanodegree.marvelpeople.herodetails;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.hussamelemmawi.nanodegree.marvelpeople.MarvelPeopleApp;
import com.hussamelemmawi.nanodegree.marvelpeople.R;
import com.hussamelemmawi.nanodegree.marvelpeople.data.source.local.model.Hero;
import com.hussamelemmawi.nanodegree.marvelpeople.util.SelectableFloatingActionButton;
import com.hussamelemmawi.nanodegree.marvelpeople.util.Utility;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.blurry.Blurry;

/**
 * Created by hussamelemmawi on 22/11/16.
 * <p>
 * Displays a detailed view fot the chosen hero.
 * Pass User actions to {@link HeroDetailsPresenter} to handle it.
 */
public class HeroDetailsFragment extends Fragment implements HeroDetailsContract.View {

  public static final String TAG = HeroDetailsFragment.class.getSimpleName();

  /**
   * {@code @Inject} to request a dependency (for a field here)
   */
  @Inject
  HeroDetailsPresenter presenter;

  @BindView(R.id.hero_image)
  ImageView heroImage;

  @BindView(R.id.hero_circular_image)
  CircleImageView heroCircleImageView;

  @BindView(R.id.name_and_desc_card_text)
  TextView nameAndDescText;

  @BindView(R.id.comic_price)
  TextView eventsAvailable;

  @BindView(R.id.favorite_fab)
  SelectableFloatingActionButton fab;

  @BindView(R.id.button_action_menu)
  ImageButton popUpMenu;

  @BindView(R.id.button_action_share)
  ImageButton shareButton;

  @BindView(R.id.more_details_card)
  TextView moreDetailsCard;

  int heroId;

  private Bitmap heroBitmap;

  public static HeroDetailsFragment newInstance() {
    return new HeroDetailsFragment();
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    ((MarvelPeopleApp) context.getApplicationContext()).getApplicationComponent().inject(this);
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Intent intent = getActivity().getIntent();
    // get hero id sent by intent
    if (intent != null) {
      if (intent.getExtras() != null)
        if (intent.getExtras().containsKey("heroId")) {
          heroId = intent.getExtras().getInt("heroId");
        }
    }
    presenter.initializePresenter(heroId);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

    View rootView = inflater.inflate(R.layout.fragment_hero_details, container, false);

    ButterKnife.bind(this, rootView);

    rootView.findViewById(R.id.button_action_nav_up).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        getActivity().onBackPressed();
      }
    });

    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (!fab.isFabSelected()) {
          presenter.saveToFavorite();
        } else {
          presenter.removeFromFavorite();
        }
        fab.setFabSelected(!fab.isFabSelected());
      }
    });

    popUpMenu.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        showFilteringPopUpMenu();
      }
    });

    shareButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        presenter.shareHero();
      }
    });

    moreDetailsCard.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        presenter.openHeroDetailsFromWeb(getContext());
      }
    });

    return rootView;
  }

  @Override
  public void onStart() {
    super.onStart();
    presenter.bind(this);
  }

  @Override
  public void onResume() {
    super.onResume();
    presenter.start();
  }

  @Override
  public void onStop() {
    super.onStop();
    presenter.unbind(this);
  }

  @Override
  public void showHero(Hero hero) {
    String imageUrl = Utility.imageUrlLarge(hero.getThumbnailPath(),
      hero.getThumbnailExtension());

    Target target = new Target() {
      @Override
      public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        Blurry.with(getContext()).from(bitmap).into(heroImage);
        heroCircleImageView.setImageBitmap(bitmap);
        heroBitmap = bitmap;
      }

      @Override
      public void onBitmapFailed(Drawable errorDrawable) {
        heroBitmap = null;
      }

      @Override
      public void onPrepareLoad(Drawable placeHolderDrawable) {
      }
    };

    Picasso.with(getContext()).load(imageUrl).into(target);

    nameAndDescText.setText(hero.getName() + ": " + hero.getDescription());

    fab.setFabSelected(hero.isFavorite());
  }

  @Override
  public void showFilteringPopUpMenu() {
    PopupMenu popup = new PopupMenu(getContext(), getActivity().findViewById(R.id.button_action_menu));
    popup.getMenuInflater().inflate(R.menu.marvel_details_menu, popup.getMenu());

    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
      public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.set_as_wallpaper_option) {
          presenter.setHeroAsWallpaper();
        }
        return true;
      }
    });

    popup.show();
  }

  @Override
  public void setAsWallpaperDone(String heroName) {
    if (heroBitmap != null) {
      WallpaperManager wallpaperManager = WallpaperManager.getInstance(getContext());
      try {
        wallpaperManager.setBitmap(heroBitmap);
      } catch (IOException e) {
        e.printStackTrace();
      }
      showMessage("Setting " + heroName + " as Wallpaper done!");
    } else {
      showMessage(getString(R.string.error_failed_set_as_wallpaper));
    }
  }

  @Override
  public void showNoHeroDataAvailable() {
    showMessage(getString(R.string.error_no_available_hero_details));
  }

  private void showMessage(String s) {
    Snackbar.make(getView(), s, Snackbar.LENGTH_LONG).show();
  }
}
