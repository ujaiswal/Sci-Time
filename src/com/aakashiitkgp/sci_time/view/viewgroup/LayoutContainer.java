package com.aakashiitkgp.sci_time.view.viewgroup;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.aakashiitkgp.sci_time.R;

public class LayoutContainer extends LinearLayout {
	
	/**
	 * References to groups contained in this view.
	 */
	View menu;
	View content;
	
	protected int MENU_MARGIN;
	
	public enum MenuState {
		CLOSED, CLOSING, OPEN, OPENING
	}
	
	/**
	 * Animation objects.
	 */
	protected Scroller MenuAnimationScroller = new Scroller(this.getContext(), new SmoothInterpolator());
	protected Runnable MenuAnimationRunnable = new AnimationRunnable();
	protected Handler MenuAnimationHandler = new Handler();
	
	/**
	 * Animation constants
	 */
	private static final int MENU_ANIMATION_DURATION = 500;
	private static final int MENU_ANIMATION_POLLING_INTERVAL = 16;
	
	/**
	 * Position information attributes.
	 */
	protected int CurrentContentOffset = 0;
	protected MenuState CurrentMenuState = MenuState.CLOSED;
	
	public LayoutContainer(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public LayoutContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public LayoutContainer(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onAttachedToWindow() {
		// TODO Auto-generated method stub
		super.onAttachedToWindow();
		
		this.menu = getChildAt(0);
		this.content = getChildAt(1);
		
		this.menu.setVisibility(View.GONE);
		
		MENU_MARGIN = (int) getResources().getDimension(R.dimen.menu_margin);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if (changed) {
			this.calculateChildDimensions();
		}
		
		this.menu.layout(l, t, r - MENU_MARGIN, b);
		
		this.content.layout(l + this.CurrentContentOffset, t, r + this.CurrentContentOffset, b);
	}

	private void calculateChildDimensions() {
		this.content.getLayoutParams().height = this.getHeight();
		this.content.getLayoutParams().width = this.getWidth();
		
		this.menu.getLayoutParams().height = this.getHeight();
		this.menu.getLayoutParams().width = this.getWidth() - MENU_MARGIN;
		
	}

	private int getMenuWidth() {
		return this.menu.getLayoutParams().width;
	}
	
	public void toggleMenu() {
		switch (this.CurrentMenuState) {
		case CLOSED:
			this.CurrentMenuState = MenuState.OPENING;
			this.menu.setVisibility(View.VISIBLE);
			this.MenuAnimationScroller.startScroll(0, 0, this.getMenuWidth(), 0, MENU_ANIMATION_DURATION);
			break;
		case OPEN:
			this.CurrentMenuState = MenuState.CLOSING;
			this.MenuAnimationScroller.startScroll(this.CurrentContentOffset, 0, -this.CurrentContentOffset, 0, MENU_ANIMATION_DURATION);
			break;
		default:
			return;
		}
		
		this.MenuAnimationHandler.postDelayed(this.MenuAnimationRunnable, MENU_ANIMATION_POLLING_INTERVAL);
	}
	
	protected class SmoothInterpolator implements Interpolator {

		@Override
		public float getInterpolation(float t) {
			return (float) Math.pow(t - 1, 5) + 1;
		}
	}
	
	protected class AnimationRunnable implements Runnable {

		@Override
		public void run() {
			boolean isAnimationOngoing = LayoutContainer.this.MenuAnimationScroller.computeScrollOffset();
			
			LayoutContainer.this.adjustContentPosition(isAnimationOngoing);
		}
	}

	public void adjustContentPosition(boolean isAnimationOngoing) {
		int ScrollerOffset = this.MenuAnimationScroller.getCurrX();
		
		this.content.offsetLeftAndRight(ScrollerOffset - this.CurrentContentOffset);
		
		this.CurrentContentOffset = ScrollerOffset;
		
		this.invalidate();
		
		if(isAnimationOngoing) {
			this.MenuAnimationHandler.postDelayed(this.MenuAnimationRunnable, MENU_ANIMATION_POLLING_INTERVAL);
		}
		else {
			this.onMenuTransitionComplete();
		}
	}

	private void onMenuTransitionComplete() {
		switch (this.CurrentMenuState) {
		case OPENING:
			this.CurrentMenuState = MenuState.OPEN;
			break;
		case CLOSING:
			this.CurrentMenuState = MenuState.CLOSED;
			this.menu.setVisibility(View.GONE);
			break;
		default:
			return;
		}
	}
}


