package com.discovertodo.phone.android.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;


import com.bumptech.glide.Glide;
import com.discovertodo.phone.android.R;
import com.discovertodo.phone.android.activity.ShowImageActivity;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.squareup.picasso.Picasso;

public class MindCardFragment extends BaseFragment {

    private static final int DURATION = 400;
    public HomeFragment parentFragment;
    public View btnFlip, btnBack, layoutTopMindCard;
    private ViewFlipper vf;
    private View mFrontView, mBackView;
    private ImageView img_card_1_1, img_card_13_1, img_card_12_1, img_card_9_1, img_card_2_1, img_card_3_1, img_card_4_1,
            img_card_5_1, img_card_6_1, img_card_7_1, img_card_8_1, img_card_10_1, img_card_11_1, img_card_14_1, img_card_1_2, img_card_2_2,
            img_card_3_2, img_card_4_2, img_card_5_2, img_card_6_2, img_card_7_2, img_card_8_2, img_card_9_2, img_card_10_2, img_card_11_2, img_card_12_2, img_card_13_2,
            img_card_14_2;
    boolean[] check = {true, true, true, true, true, true, true, true, true, true, true, true, true, true};
    private int flag;
    private LinearLayout imv1_1, imv2_1 ,imv3_1 ,imv4_1 , imv5_1 , imv6_1 ,imv7_1, imv8_1 , imv9_1 , imv10_1 , imv11_1 , imv12_1 ,imv13_1 , imv14_1 , imv5_2 ,  imv7_2 ,imv14_2;
    private int checkImage=1;
    private int heightPixel;
    private int widthPixel;


    public static MindCardFragment getInstance(HomeFragment fragment) {
        MindCardFragment mindCardFragment = new MindCardFragment();
        mindCardFragment.parentFragment = fragment;
        mindCardFragment.layoutTopMindCard = fragment.layoutTopMindCard;
        return mindCardFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        layoutTopMindCard.setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.mind_card_info, container, false);
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            heightPixel = displayMetrics.heightPixels;
            widthPixel = displayMetrics.widthPixels;
            setupLayout();
            if (vf.getDisplayedChild() == 0) {

                mFrontView = view.findViewById(R.id.card1_1);
                mBackView = view.findViewById(R.id.card1_2);
                btnFlip.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        flipCard(0);
                    }
                });
                setImageListener(img_card_1_1);
                setImageListener(img_card_1_2);
            }
        }

        return super.onCreateView(inflater, container, savedInstanceState);
    }


    public void flip(final View front, final View back, final int duration) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            AnimatorSet set = new AnimatorSet();
            set.playSequentially(
                    ObjectAnimator.ofFloat(front, "rotationY", 90).setDuration(duration / 2),
                    ObjectAnimator.ofInt(front, "visibility", View.GONE).setDuration(0),
                    ObjectAnimator.ofFloat(back, "rotationY", -90).setDuration(0),
                    ObjectAnimator.ofInt(back, "visibility", View.VISIBLE).setDuration(0),
                    ObjectAnimator.ofFloat(back, "rotationY", 0).setDuration(duration / 2)
            );
            set.start();
        } else {
            front.animate().rotationY(90).setDuration(duration / 2).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    front.setVisibility(View.GONE);
                    back.setRotationY(-90);
                    back.setVisibility(View.VISIBLE);
                    back.animate().rotationY(0).setDuration(duration / 2).setListener(null);
                }
            });
        }
    }


    private void initCard(final int a, ImageView imv1, ImageView imv2, int frontView, int backView) {
        mFrontView = view.findViewById(frontView);
        mBackView = view.findViewById(backView);
        btnFlip.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                flipCard(a);
            }
        });
        setImageListener(imv1);
        setImageListener(imv2);


    }

    private void setFlip(int flag, int card1_back, int card1_front, int card3_back, int card3_front) {
        if (flag == 0) {
            if (!check[flag + 1]) {
                flip(view.findViewById(card3_back), view.findViewById(card3_front), DURATION);
            }
        }
        if (flag != 0 && flag != 13) {
            if (!check[flag - 1]) {
                flip(view.findViewById(card1_back), view.findViewById(card1_front), DURATION);
            }
            if (!check[flag + 1]) {
                flip(view.findViewById(card3_back), view.findViewById(card3_front), DURATION);
            }
        }
        if (flag == 13) {
            if (!check[flag - 1]) {
                flip(view.findViewById(card1_back), view.findViewById(card1_front), DURATION);
            }
        }
        setTrue(flag);
    }

    public void setCard() {
        switch (vf.getDisplayedChild()) {
            case 0:
                flag = 0;
                initCard(flag, img_card_1_1, img_card_1_2, R.id.card1_1, R.id.card1_2);
                setFlip(flag, 0, 0, R.id.card2_2, R.id.card2_1);
                break;
            case 1:
                flag = 1;
                initCard(flag, img_card_2_1, img_card_2_2, R.id.card2_1, R.id.card2_2);
                setFlip(flag, R.id.card1_2, R.id.card1_1, R.id.card3_2, R.id.card3_1);
                break;
            case 2:
                flag = 2;
                initCard(flag, img_card_3_1, img_card_3_2, R.id.card3_1, R.id.card3_2);
                setFlip(flag, R.id.card2_2, R.id.card2_1, R.id.card4_2, R.id.card4_1);
                break;
            case 3:
                flag = 3;
                initCard(flag, img_card_4_1, img_card_4_2, R.id.card4_1, R.id.card4_2);
                setFlip(flag, R.id.card3_2, R.id.card3_1, R.id.card5_2, R.id.card5_1);
                break;
            case 4:
                flag = 4;
                initCard(flag, img_card_5_1, img_card_5_2, R.id.card5_1, R.id.card5_2);
                setFlip(flag, R.id.card4_2, R.id.card4_1, R.id.card6_2, R.id.card6_1);
                break;
            case 5:
                flag = 5;
                initCard(flag, img_card_6_1, img_card_6_2, R.id.card6_1, R.id.card6_2);
                setFlip(flag, R.id.card5_2, R.id.card5_1, R.id.card7_2, R.id.card7_1);
                break;
            case 6:
                flag = 6;
                initCard(flag, img_card_7_1, img_card_7_2, R.id.card7_1, R.id.card7_2);
                setFlip(flag, R.id.card6_2, R.id.card6_1, R.id.card8_2, R.id.card8_1);
                break;
            case 7:
                flag = 7;
                initCard(flag, img_card_8_1, img_card_8_2, R.id.card8_1, R.id.card8_2);
                setFlip(flag, R.id.card7_2, R.id.card7_1, R.id.card9_2, R.id.card9_1);
                break;
            case 8:
                flag = 8;
                initCard(flag, img_card_9_1, img_card_9_2, R.id.card9_1, R.id.card9_2);
                setFlip(flag, R.id.card8_2, R.id.card8_1, R.id.card10_2, R.id.card10_1);
                break;
            case 9:
                flag = 9;
                initCard(flag, img_card_10_1, img_card_10_2, R.id.card10_1, R.id.card10_2);
                setFlip(flag, R.id.card9_2, R.id.card9_1, R.id.card11_2, R.id.card11_1);
                break;
            case 10:
                flag = 10;
                initCard(flag, img_card_11_1, img_card_11_2, R.id.card11_1, R.id.card11_2);
                setFlip(flag, R.id.card10_2, R.id.card10_1, R.id.card12_2, R.id.card12_1);
                break;
            case 11:
                flag = 11;
                initCard(flag, img_card_12_1, img_card_12_2, R.id.card12_1, R.id.card12_2);
                setFlip(flag, R.id.card11_2, R.id.card11_1, R.id.card13_2, R.id.card13_1);
                break;
            case 12:
                flag = 12;
                initCard(flag, img_card_13_1, img_card_13_2, R.id.card13_1, R.id.card13_2);
                setFlip(flag, R.id.card12_2, R.id.card12_1, R.id.card14_2, R.id.card14_1);
                break;
            case 13:
                flag = 13;
                initCard(flag, img_card_14_1, img_card_14_2, R.id.card14_1, R.id.card14_2);
                setFlip(flag, R.id.card13_2, R.id.card13_1, 0, 0);
                break;


        }
        Log.i("getDisplayedChild()", vf.getDisplayedChild() + "");
    }


    private void setTrue(int a) {
        for (int i = 0; i < check.length; i++) {
            if (i != a) {
                check[i] = true;
            }
        }
    }

    private void flipCard(int i) {
        if (check[i]) {
            flip(mFrontView, mBackView, DURATION);
            check[i] = false;
        } else {
            flip(mBackView, mFrontView, DURATION);
            check[i] = true;
        }
    }

    public void setupLayout() {
        btnFlip = parentFragment.getBtnFlipcard();
        btnBack = parentFragment.btnBackCard;
        vf = (ViewFlipper) view.findViewById(R.id.view_flipper);
        img_card_1_1 = (ImageView) view.findViewById(R.id.img_card_1_1);
        img_card_2_1 = (ImageView) view.findViewById(R.id.img_card_2_1);
        img_card_3_1 = (ImageView) view.findViewById(R.id.img_card_3_1);
        img_card_4_1 = (ImageView) view.findViewById(R.id.img_card_4_1);
        img_card_5_1 = (ImageView) view.findViewById(R.id.img_card_5_1);
        img_card_6_1 = (ImageView) view.findViewById(R.id.img_card_6_1);
        img_card_7_1 = (ImageView) view.findViewById(R.id.img_card_7_1);
        img_card_8_1 = (ImageView) view.findViewById(R.id.img_card_8_1);
        img_card_9_1 = (ImageView) view.findViewById(R.id.img_card_9_1);
        img_card_10_1 = (ImageView) view.findViewById(R.id.img_card_10_1);
        img_card_11_1 = (ImageView) view.findViewById(R.id.img_card_11_1);
        img_card_12_1 = (ImageView) view.findViewById(R.id.img_card_12_1);
        img_card_13_1 = (ImageView) view.findViewById(R.id.img_card_13_1);
        img_card_14_1 = (ImageView) view.findViewById(R.id.img_card_14_1);

        img_card_1_2 = (ImageView) view.findViewById(R.id.img_card_1_2);
        img_card_2_2 = (ImageView) view.findViewById(R.id.img_card_2_2);
        img_card_3_2 = (ImageView) view.findViewById(R.id.img_card_3_2);
        img_card_4_2 = (ImageView) view.findViewById(R.id.img_card_4_2);
        img_card_5_2 = (ImageView) view.findViewById(R.id.img_card_5_2);
        img_card_6_2 = (ImageView) view.findViewById(R.id.img_card_6_2);
        img_card_7_2 = (ImageView) view.findViewById(R.id.img_card_7_2);
        img_card_8_2 = (ImageView) view.findViewById(R.id.img_card_8_2);
        img_card_9_2 = (ImageView) view.findViewById(R.id.img_card_9_2);
        img_card_10_2 = (ImageView) view.findViewById(R.id.img_card_10_2);
        img_card_11_2 = (ImageView) view.findViewById(R.id.img_card_11_2);
        img_card_12_2 = (ImageView) view.findViewById(R.id.img_card_12_2);
        img_card_13_2 = (ImageView) view.findViewById(R.id.img_card_13_2);
        img_card_14_2 = (ImageView) view.findViewById(R.id.img_card_14_2);
        setLayoutImageClick();
        setImage(img_card_1_1, R.drawable.mind_card1_1);
        setImage(img_card_1_2, R.drawable.mind_card1_2);
        setImage(img_card_2_1, R.drawable.mind_card2_1);
        setImage(img_card_3_1, R.drawable.mind_card3_1);
        setImage(img_card_4_1, R.drawable.mind_card4_1);
        setImage(img_card_5_1, R.drawable.mind_card5_1);
        setImage(img_card_6_1, R.drawable.mind_card6_1);
        setImage(img_card_7_1, R.drawable.mind_card7_1);
        setImage(img_card_8_1, R.drawable.mind_card8_1);
        setImage(img_card_9_1, R.drawable.mind_card9_1);
        setImage(img_card_10_1, R.drawable.mind_card10_1);
        setImage(img_card_11_1, R.drawable.mind_card11_1);
        setImage(img_card_12_1, R.drawable.mind_card12_1);
        setImage(img_card_13_1, R.drawable.mind_card13_1);
        setImage(img_card_14_1, R.drawable.mind_card14_1);



        setImage(img_card_2_2, R.drawable.mind_card2_2);
        setImage(img_card_3_2, R.drawable.mind_card3_2);
        setImage(img_card_4_2, R.drawable.mind_card4_2);
        setImage(img_card_5_2, R.drawable.mind_card5_2);
        setImage(img_card_6_2, R.drawable.mind_card6_2);
        setImage(img_card_7_2, R.drawable.mind_card7_2);
        setImage(img_card_8_2, R.drawable.mind_card8_2);
        setImage(img_card_9_2, R.drawable.mind_card9_2);
        setImage(img_card_10_2, R.drawable.mind_card10_2);
        setImage(img_card_11_2, R.drawable.mind_card11_2);
        setImage(img_card_12_2, R.drawable.mind_card12_2);
        setImage(img_card_13_2, R.drawable.mind_card13_2);
        setImage(img_card_14_2, R.drawable.mind_card14_2);



    }

    private void setLayoutImageClick() {
        imv1_1 = (LinearLayout) view.findViewById(R.id.imv_1_1);
        imv2_1 = (LinearLayout) view.findViewById(R.id.imv_2_1);
        imv3_1 = (LinearLayout) view.findViewById(R.id.imv_3_1);
        imv4_1 = (LinearLayout) view.findViewById(R.id.imv_4_1);
        imv5_1 = (LinearLayout) view.findViewById(R.id.imv_5_1);
        imv6_1 = (LinearLayout) view.findViewById(R.id.imv_6_1);
        imv7_1 = (LinearLayout) view.findViewById(R.id.imv_7_1);
        imv8_1 = (LinearLayout) view.findViewById(R.id.imv_8_1);
        imv9_1 = (LinearLayout) view.findViewById(R.id.imv_9_1);
        imv10_1 = (LinearLayout) view.findViewById(R.id.imv_10_1);
        imv11_1 = (LinearLayout) view.findViewById(R.id.imv_11_1);
        imv12_1 = (LinearLayout) view.findViewById(R.id.imv_12_1);
        imv13_1 = (LinearLayout) view.findViewById(R.id.imv_13_1);
        imv14_1 = (LinearLayout) view.findViewById(R.id.imv_14_1);
        imv5_2 = (LinearLayout) view.findViewById(R.id.imv_5_2);
        imv7_2 = (LinearLayout) view.findViewById(R.id.imv_7_2);
        imv14_2 = (LinearLayout) view.findViewById(R.id.imv_14_2);
        setLayoutListener( imv1_1,R.drawable.imv1_1);
        setLayoutListener(imv2_1, R.drawable.imv2_1);
        setLayoutListener(imv3_1, R.drawable.imv3_1);
        setLayoutListener(imv4_1, R.drawable.imv4_1);
        setLayoutListener(imv5_1, R.drawable.imv5_1);
        setLayoutListener(imv6_1, R.drawable.imv6_1);
        setLayoutListener(imv7_1, R.drawable.imv7_1);
        setLayoutListener(imv8_1, R.drawable.imv8_1);
        setLayoutListener(imv9_1, R.drawable.imv9_1);
        setLayoutListener(imv10_1, R.drawable.imv10_1);
        setLayoutListener(imv11_1, R.drawable.imv11_1);
        setLayoutListener(imv12_1, R.drawable.imv12_1);
        setLayoutListener(imv13_1, R.drawable.imv13_1);
        setLayoutListener(imv14_1, R.drawable.imv14_1);
        setLayoutListener(imv5_2, R.drawable.imv5_2);
        setLayoutListener(imv7_2, R.drawable.imv7_2);
        setLayoutListener(imv14_2, R.drawable.imv14_2);

    }

    private void setLayoutListener(LinearLayout imv, final int id) {
//        layout.setSoundEffectsEnabled(false);
//        layout.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
//            @Override
//            public void onSwipeRight() {
//                super.onSwipeRight();
//            }
//
//            @Override
//            public void onSwipeLeft() {
//                super.onSwipeLeft();
//            }
//
//            @Override
//            public void onDoubleClick() {
//                super.onDoubleClick();
//            }
//        });
        imv.setSoundEffectsEnabled(false);
        imv.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
            }

            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
            }

            @Override
            public void onDoubleClick() {
                super.onDoubleClick();
                setClickImage(id);





            }
        });
    }

    private void setImage(ImageView imv, int id) {
        Glide.with(getActivity()).load(id).asBitmap().override(widthPixel, heightPixel).into(imv);
//        Picasso.with(getActivity().getApplicationContext()).load(id).fit().into(imv);
    }

    @Override
    public void onStart() {
        super.onStart();
        layoutTopMindCard.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStop() {
        super.onStop();
        layoutTopMindCard.setVisibility(View.GONE);
    }


    public class OnSwipeTouchListener implements View.OnTouchListener {

        private GestureDetector gestureDetector;

        public OnSwipeTouchListener(Context c) {
            gestureDetector = new GestureDetector(c, new GestureListener());
        }

        public boolean onTouch(final View view, final MotionEvent motionEvent) {
            return gestureDetector.onTouchEvent(motionEvent);
        }

        private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

            private static final int SWIPE_THRESHOLD = 80;
            private static final int SWIPE_VELOCITY_THRESHOLD = 80;

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }


            @Override
            public boolean onDoubleTap(MotionEvent e) {
                onDoubleClick();
                return super.onDoubleTap(e);
            }


            // Determines the fling velocity and then fires the appropriate swipe event accordingly
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                boolean result = false;
                try {
                    float diffY = e2.getY() - e1.getY();
                    float diffX = e2.getX() - e1.getX();
                    if (Math.abs(diffX) > Math.abs(diffY)) {
                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffX > 0) {
                                onSwipeRight();
                            } else {
                                onSwipeLeft();
                            }
                        }
                        result = true;
                    }

                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                return result;
            }
        }

        public void onSwipeRight() {
            if (vf.getDisplayedChild() == 0) {
                return;
            } else {
                vf.setInAnimation(activity, R.anim.in_from_left);
                vf.setOutAnimation(activity, R.anim.out_to_right);
                vf.showPrevious();
            }
            setCard();

        }

        public void onSwipeLeft() {
            if (vf.getDisplayedChild() == 13) {
                return;
            } else {
                vf.setInAnimation(activity, R.anim.in_from_right);
                vf.setOutAnimation(activity, R.anim.out_to_left);

                vf.showNext();
            }
            setCard();

        }

        public void onDoubleClick() {

        }

    }

    public void setClickImage(int path) {
        Intent intent = new Intent(getActivity(), ShowImageActivity.class);
        intent.putExtra("path", path);
        startActivity(intent);
    }

    public void setImageListener(ImageView img) {
        img.setSoundEffectsEnabled(false);
        img.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
            }

            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
            }

            @Override
            public void onDoubleClick() {
                super.onDoubleClick();
            }
        });
    }


}
