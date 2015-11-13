package com.canteratech.androidtest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DragAndDropPlayground extends RelativeLayout {

	private enum Status {
		PAINTING, DRAGGING, STOPPED
	}

	public enum DraggedSource {
		FROM_CONTAINER, FROM_LIST, FROM_BUCKET
	}

	private Status status = Status.STOPPED;

	private View draggedView;

	private Object draggedObject;

	private DraggedSource draggedSource;

	private Bitmap floatingBitmap;

	private ImageView floatingImage;

	private LayoutParams floatingLayoutParams;

	private DropZone actualDropZone;

	private List<DropZone> dropZones;

	private int[] locationPlayground = new int[2];

	private int[] locationDraggedView = new int[2];

	private boolean xMovement = true;

	private boolean yMovement = true;

	private List<Bucket> buckets;

	private OnDragAndDropListener onDragAndDropListener;

	public DragAndDropPlayground(Context context) {
		super(context);
	}

	public DragAndDropPlayground(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public DragAndDropPlayground(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public void initialize(View container, View bucketsList, View itemsList, List<Bucket> buckets, OnDragAndDropListener onDragAndDropListener) {
		dropZones = new ArrayList<>();
		dropZones.add(new ContainerDropZone(container));
		dropZones.add(new BucketListDropZone(bucketsList));
		dropZones.add(new ListDropZone(itemsList));
		this.buckets = buckets;
		this.onDragAndDropListener = onDragAndDropListener;
	}

	public void startDrag(View toDrag, DraggedSource draggedSource, Object draggedObject) {

		// Initialize data
		this.draggedView = toDrag;
		this.draggedSource = draggedSource;
		this.draggedObject = draggedObject;
		this.actualDropZone = null;

		// Calculate location of playground
		getLocationOnScreen(locationPlayground);
		draggedView.getLocationOnScreen(locationDraggedView);

		// Get image of element to drag
		toDrag.setDrawingCacheEnabled(true);
		floatingBitmap = Bitmap.createBitmap(toDrag.getDrawingCache());
		toDrag.setDrawingCacheEnabled(false);

		// Create floating image
		floatingImage = new ImageView(getContext());
		floatingImage.setScaleType(ImageView.ScaleType.MATRIX);
		floatingImage.setPadding(0, 0, 0, 0);

		// Positioning floating image
		floatingLayoutParams = new LayoutParams(draggedView.getWidth(), draggedView.getHeight());

		int x = locationDraggedView[0];
		int y = locationDraggedView[1];
		floatingLayoutParams.leftMargin = x - locationPlayground[0];
		floatingLayoutParams.topMargin = y - locationPlayground[1];
		floatingImage.setLayoutParams(floatingLayoutParams);

		// Add image
		addView(floatingImage);

		for (DropZone dropZone : dropZones) {
			dropZone.notify(DragEvent.ACTION_DRAG_STARTED);
		}

		determineDropZone(x, y);
		paintFloatingView();

		draggedView.setVisibility(View.INVISIBLE);
	}

	@Override
	public boolean onTouchEvent(MotionEvent motionEvent) {
		switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				stop();
				break;
			case MotionEvent.ACTION_MOVE:
				if (status == Status.DRAGGING) {
					int x = (int) motionEvent.getRawX();
					int y = (int) motionEvent.getRawY();
					notifyDropZone(x, y);
					move(x, y);
				}
				break;
		}
		return super.onTouchEvent(motionEvent);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
		return status != Status.STOPPED;
	}

	private void move(int x, int y) {
		if (xMovement) {
			floatingLayoutParams.leftMargin = x - (floatingLayoutParams.width / 2) - locationPlayground[0];
		} else {
			floatingLayoutParams.leftMargin = locationDraggedView[0] - locationPlayground[0];
		}
		if (yMovement) {
			floatingLayoutParams.topMargin = y - (floatingLayoutParams.height / 2) - locationPlayground[1];
		} else {
			floatingLayoutParams.topMargin = locationDraggedView[1] - locationPlayground[1];
		}
		floatingImage.setLayoutParams(floatingLayoutParams);
	}

	private void stop() {
		if (actualDropZone != null) {
			actualDropZone.notify(DragEvent.ACTION_DROP);
		}
		for (DropZone dropZone : dropZones) {
			dropZone.notify(DragEvent.ACTION_DRAG_ENDED);
		}
		status = Status.STOPPED;
		if (draggedView != null) {
			draggedView.setVisibility(View.VISIBLE);
		}
		draggedView = null;
		actualDropZone = null;
		if (floatingImage != null) {
			removeView(floatingImage);
		}
		floatingImage = null;
		floatingLayoutParams = null;
		draggedObject = null;
	}

	private void notifyDropZone(int x, int y) {
		boolean changed = determineDropZone(x, y);
		if (changed) {
			paintFloatingView();
		}
		if (actualDropZone != null) {
			actualDropZone.notify(DragEvent.ACTION_DRAG_LOCATION, x, y);
		}
	}

	private void paintFloatingView() {
		status = Status.PAINTING;
		if (actualDropZone == null) {
			paintDefaultFloatingView();
		} else {
			floatingImage.setVisibility(View.VISIBLE);
			actualDropZone.notify(DragEvent.ACTION_DRAG_ENTERED);
		}
		status = Status.DRAGGING;
	}

	private void paintDefaultFloatingView() {
		if (draggedSource != DraggedSource.FROM_BUCKET) {
			floatingImage.setVisibility(View.INVISIBLE);
		}
	}

	private boolean determineDropZone(int x, int y) {
		DropZone newDropZone = null;
		for (DropZone dropZone : dropZones) {
			if (dropZone.isInside(x, y)) {
				newDropZone = dropZone;
				break;
			}
		}
		boolean isNew = (newDropZone != actualDropZone);
		DropZone oldDropZone = actualDropZone;
		actualDropZone = newDropZone;
		if (isNew && oldDropZone != null) {
			oldDropZone.notify(DragEvent.ACTION_DRAG_EXITED);
		}
		return isNew;
	}

	private abstract class DropZone {

		protected View view;

		protected int[] location = new int[2];

		public DropZone(View view) {
			this.view = view;
		}

		public boolean isInside(int x, int y) {
			return !(x < location[0] || y < location[1]) && x <= view.getWidth() + location[0] && y <= view.getHeight() + location[1];
		}

		public final void notify(int dragEvent) {
			notify(dragEvent, -1, -1);
		}

		public void notify(int dragEvent, int x, int y) {
			switch (dragEvent) {
				case DragEvent.ACTION_DRAG_STARTED:
					view.getLocationOnScreen(location);
					break;
				case DragEvent.ACTION_DRAG_ENTERED:
					break;
				case DragEvent.ACTION_DRAG_EXITED:
					break;
				case DragEvent.ACTION_DRAG_LOCATION:
					break;
				case DragEvent.ACTION_DRAG_ENDED:
					break;
				case DragEvent.ACTION_DROP:
					break;
			}
			onNotified(dragEvent, x, y);
		}

		protected abstract void onNotified(int dragEvent, int x, int y);
	}

	private class ContainerDropZone extends DropZone {

		private ViewGroup container;

		private ImageView trash;

		public ContainerDropZone(View view) {
			super(view);
			this.container = (ViewGroup) view;
		}

		@Override
		protected void onNotified(int dragEvent, int x, int y) {
			switch (dragEvent) {
				case DragEvent.ACTION_DRAG_STARTED:
					if (draggedSource != DraggedSource.FROM_CONTAINER) {
						trash = new ImageView(getContext());
						trash.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
						trash.setImageResource(android.R.drawable.ic_menu_delete);
						trash.setScaleType(ImageView.ScaleType.CENTER);
						trash.setBackgroundColor(Color.parseColor("#55000000"));
						container.addView(trash);
					}
					break;
				case DragEvent.ACTION_DRAG_ENTERED:
					if (draggedSource == DraggedSource.FROM_CONTAINER) {
						xMovement = true;
						yMovement = true;
						floatingLayoutParams.width = draggedView.getWidth();
						floatingLayoutParams.height = draggedView.getHeight();
						floatingImage.setImageBitmap(floatingBitmap);
					} else if (trash != null) {
						floatingImage.setVisibility(View.INVISIBLE);
						trash.setImageResource(android.R.drawable.ic_delete);
					}
					break;
				case DragEvent.ACTION_DRAG_EXITED:
					if (draggedSource != DraggedSource.FROM_CONTAINER && trash != null) {
						trash.setImageResource(android.R.drawable.ic_menu_delete);
					}
					break;
				case DragEvent.ACTION_DRAG_LOCATION:
					break;
				case DragEvent.ACTION_DRAG_ENDED:
					if (trash != null) {
						container.removeView(trash);
						trash = null;
					}
					break;
				case DragEvent.ACTION_DROP:
					if (draggedSource == DraggedSource.FROM_LIST) {
						BucketItem bucketItem = (BucketItem) draggedObject;
						onDragAndDropListener.onRemoveItemFromBucket(bucketItem);
					} else if (draggedSource == DraggedSource.FROM_BUCKET) {
						Bucket bucket = (Bucket) draggedObject;
						onDragAndDropListener.onRemoveBucket(bucket);
					}
					break;
			}
		}
	}

	private class BucketListDropZone extends DropZone {

		private ViewGroup container;

		public BucketListDropZone(View view) {
			super(view);
			this.container = (ViewGroup) view;
		}

		@Override
		protected void onNotified(int dragEvent, int x, int y) {
			switch (dragEvent) {
				case DragEvent.ACTION_DRAG_STARTED:
					break;
				case DragEvent.ACTION_DRAG_ENTERED:
					if (draggedSource == DraggedSource.FROM_BUCKET) {
						floatingLayoutParams.width = draggedView.getWidth();
						floatingLayoutParams.height = draggedView.getHeight();
						floatingImage.setImageBitmap(floatingBitmap);
						xMovement = true;
						yMovement = false;
					} else {
						xMovement = true;
						yMovement = true;
						floatingLayoutParams.width = LayoutParams.WRAP_CONTENT;
						floatingLayoutParams.height = LayoutParams.WRAP_CONTENT;
						floatingImage.setImageResource(android.R.drawable.arrow_down_float);
					}
					break;
				case DragEvent.ACTION_DRAG_EXITED:
					break;
				case DragEvent.ACTION_DRAG_LOCATION:
					if (draggedSource == DraggedSource.FROM_BUCKET) {
						moveAdditionalViewToPosition(x);
					} else {
						onDragAndDropListener.onEnterBucket(getBucketByLocation(x));
					}
					break;
				case DragEvent.ACTION_DRAG_ENDED:
					break;
				case DragEvent.ACTION_DROP:
					if (draggedSource != DraggedSource.FROM_BUCKET) {
						BucketItem bucketItem = (BucketItem) draggedObject;
						for (Bucket bucket : buckets) {
							if (bucket.isActive() && bucket != bucketItem.getBucket()) {
								if (bucketItem.getBucket() != null) {
									onDragAndDropListener.onRemoveItemFromBucket(bucketItem);
								}
								bucket.getAdditionalView().setVisibility(View.VISIBLE);
								bucketItem.setView(bucket.getAdditionalView());
								bucket.setAdditionalView(null);
								onDragAndDropListener.onAddItemToBucket(bucket, bucketItem);
							}
						}
					}
					break;
			}
		}

		private Bucket getBucketByLocation(int x) {
			for (int iter = 0; iter < container.getChildCount(); iter++) {
				View view = container.getChildAt(iter);
				int[] locationView = new int[2];
				view.getLocationOnScreen(locationView);
				if (locationView[0] + view.getWidth() > x) {
					for (Bucket bucket : buckets) {
						if (bucket.getBucketView() == view) {
							return bucket;
						}
					}
				}
			}
			return null;
		}

		public void moveAdditionalViewToPosition(int x) {
			int actualPosition = container.indexOfChild(draggedView);
			int newPosition = container.getChildCount() - 1;
			for (int iter = 0; iter < container.getChildCount(); iter++) {
				View view = container.getChildAt(iter);
				int[] locationView = new int[2];
				view.getLocationOnScreen(locationView);
				if (locationView[0] + (view.getWidth() / 2) > x) {
					newPosition = iter;
					break;
				}
			}
			if (actualPosition != newPosition) {
				container.removeView(draggedView);
				container.addView(draggedView, newPosition);
			}
		}
	}

	private class ListDropZone extends DropZone {

		private Bucket activeBucket;

		private BucketListDropZone bucketListDropZone;

		public ListDropZone(View view) {
			super(view);
		}

		@Override
		protected void onNotified(int dragEvent, int x, int y) {
			switch (dragEvent) {
				case DragEvent.ACTION_DRAG_STARTED:
					if (draggedSource != DraggedSource.FROM_BUCKET) {
						for (final Bucket bucket : buckets) {
							BucketItem bucketItem = (BucketItem) draggedObject;
							if (draggedSource == DraggedSource.FROM_LIST && bucket == bucketItem.getBucket()) {
								bucket.setAdditionalView(bucketItem.getView());
							} else {
								final TextView additionalItemList = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.item_list, bucket.getList(), false);
								additionalItemList.setText(bucketItem.getName());
								additionalItemList.setBackgroundColor(getResources().getColor(bucket.getColor()));
								additionalItemList.setVisibility(View.INVISIBLE);
								bucket.getList().addView(additionalItemList, 0);
								bucket.setAdditionalView(additionalItemList);
							}
						}
					}
					break;
				case DragEvent.ACTION_DRAG_ENTERED:
					if (draggedSource != DraggedSource.FROM_BUCKET) {
						for (Bucket bucket : buckets) {
							if (bucket.isActive()) {
								activeBucket = bucket;
							}
						}
						BucketItem bucketItem = (BucketItem) draggedObject;
						if (draggedSource == DraggedSource.FROM_LIST && activeBucket == bucketItem.getBucket()) {
							floatingLayoutParams.width = draggedView.getWidth();
							floatingLayoutParams.height = draggedView.getHeight();
							floatingImage.setImageBitmap(floatingBitmap);
							xMovement = false;
							yMovement = true;
						} else if (activeBucket != null){
							View additionalItemList = activeBucket.getAdditionalView();
							additionalItemList.setDrawingCacheEnabled(true);
							Bitmap bitmap = Bitmap.createBitmap(additionalItemList.getDrawingCache());
							additionalItemList.setDrawingCacheEnabled(false);
							floatingImage.setImageBitmap(bitmap);
							floatingLayoutParams.width = LayoutParams.MATCH_PARENT;
							floatingLayoutParams.height = LayoutParams.WRAP_CONTENT;
							xMovement = false;
							yMovement = true;
						}
					}
					break;
				case DragEvent.ACTION_DRAG_EXITED:
					break;
				case DragEvent.ACTION_DRAG_LOCATION:
					if (draggedSource == DraggedSource.FROM_BUCKET) {
						if (bucketListDropZone == null) {
							for (DropZone dropZone : dropZones) {
								if (dropZone instanceof BucketListDropZone) {
									bucketListDropZone = (BucketListDropZone) dropZone;
								}
							}
						}
						if (bucketListDropZone != null) {
							bucketListDropZone.moveAdditionalViewToPosition(x);
						}
					} else {
						moveAdditionalViewToPosition(y);
					}
					break;
				case DragEvent.ACTION_DRAG_ENDED:
					for (final Bucket bucket : buckets) {
						if (bucket.getAdditionalView() != null) {
							BucketItem bucketItem = (BucketItem) draggedObject;
							if (draggedSource != DraggedSource.FROM_LIST || bucket != bucketItem.getBucket()) {
								bucket.getList().removeView(bucket.getAdditionalView());
							}
						}
						bucket.setAdditionalView(null);
					}
					break;
				case DragEvent.ACTION_DROP:
					if (draggedSource != DraggedSource.FROM_BUCKET) {
						BucketItem bucketItem = (BucketItem) draggedObject;
						if (draggedSource != DraggedSource.FROM_LIST || activeBucket != bucketItem.getBucket()) {
							if (bucketItem.getBucket() != null) {
								onDragAndDropListener.onRemoveItemFromBucket(bucketItem);
							}
							activeBucket.getAdditionalView().setVisibility(View.VISIBLE);
							bucketItem.setView(activeBucket.getAdditionalView());
							activeBucket.setAdditionalView(null);
							onDragAndDropListener.onAddItemToBucket(activeBucket, bucketItem);
						}
					}
					break;
			}
		}

		private void moveAdditionalViewToPosition(int y) {
			if (activeBucket == null) {
				return;
			}
			View additionalView = activeBucket.getAdditionalView();
			int actualPosition = activeBucket.getList().indexOfChild(additionalView);
			int newPosition = activeBucket.getList().getChildCount() - 1;
			for (int iter = 0; iter < activeBucket.getList().getChildCount(); iter++) {
				View view = activeBucket.getList().getChildAt(iter);
				int[] locationView = new int[2];
				view.getLocationOnScreen(locationView);
				if (locationView[1] + (view.getHeight() / 2) > y) {
					newPosition = iter;
					break;
				}
			}
			if (actualPosition != newPosition) {
				activeBucket.getList().removeView(additionalView);
				activeBucket.getList().addView(additionalView, newPosition);
			}
		}
	}

	public interface OnDragAndDropListener {
		void onEnterBucket(Bucket bucket);
		void onRemoveBucket(Bucket bucket);
		void onAddItemToBucket(Bucket bucket, BucketItem bucketItem);
		void onRemoveItemFromBucket(BucketItem bucketItem);
	}
}
