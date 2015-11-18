package com.canteratech.androidtest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DragAndDropActivity extends AppCompatActivity {

	private DragAndDropPlayground dragAndDropPlayground;

	private ViewGroup bucketsViewGroup;

	private ViewGroup listsItemsContainer;

	private List<Bucket> buckets;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_draganddrop);
		dragAndDropPlayground = (DragAndDropPlayground) findViewById(R.id.playground);

		bucketsViewGroup = (ViewGroup) findViewById(R.id.buckets);
		listsItemsContainer = (ViewGroup) findViewById(R.id.list);

		buckets = new ArrayList<>();
		addBucket("Bucket 1", android.R.color.holo_red_light, "Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6", "Item 7");
		addBucket("Bucket 2", android.R.color.holo_orange_light, "Another Item 1", "Another Item 2", "Another Item 3");
		addBucket("Bucket 3", android.R.color.holo_blue_light, "More Items");

		findViewById(R.id.marker).setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				BucketItem bucketItem = new BucketItem();
				bucketItem.setName("Marker Added");
				dragAndDropPlayground.startDrag(v, DragAndDropPlayground.DraggedSource.FROM_CONTAINER, bucketItem);
				return false;
			}
		});

		dragAndDropPlayground.initialize(findViewById(R.id.container), bucketsViewGroup, listsItemsContainer, buckets, new DragAndDropPlayground.OnDragAndDropListener() {
			@Override
			public void onEnterBucket(Bucket bucket) {
				if (bucket != null) {
					showList(bucket);
				}
			}

			@Override
			public void onRemoveBucket(Bucket bucket) {
				if (bucket != null) {
					removeBucket(bucket);
				}
			}

			@Override
			public void onAddItemToBucket(Bucket bucket, BucketItem bucketItem) {
				addItemToBucket(bucket, bucketItem);
			}

			@Override
			public void onRemoveItemFromBucket(BucketItem bucketItem) {
				removeItemfromBucket(bucketItem);
			}
		});

		showList(buckets.get(0));
	}

	private void addBucket(String name, int color, String... items) {
		final Bucket bucket = new Bucket();
		bucket.setName(name);
		bucket.setColor(color);

		TextView bucketView = (TextView) getLayoutInflater().inflate(R.layout.bucket_item, bucketsViewGroup, false);
		bucketView.setText(name);
		bucketView.setBackgroundColor(getResources().getColor(color));
		bucketsViewGroup.addView(bucketView);
		bucket.setBucketView(bucketView);

		ViewGroup scroll = (ViewGroup) getLayoutInflater().inflate(R.layout.bucket_list, listsItemsContainer, false);
		listsItemsContainer.addView(scroll);
		bucket.setContainer(scroll);

		ViewGroup list = (ViewGroup) scroll.findViewById(R.id.linear_list_items);
		bucket.setList(list);
		bucket.setItems(new ArrayList<BucketItem>());
		for (String item : items) {
			final BucketItem bucketItem = new BucketItem();
			bucketItem.setName(item);
			bucketItem.setBucket(bucket);
			TextView textView = (TextView) getLayoutInflater().inflate(R.layout.item_list, list, false);
			textView.setText(item);
			textView.setBackgroundColor(getResources().getColor(color));
			list.addView(textView);
			textView.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View view) {
					dragAndDropPlayground.startDrag(view, DragAndDropPlayground.DraggedSource.FROM_LIST, bucketItem);
					return false;
				}
			});
			bucketItem.setView(textView);
			bucket.getItems().add(bucketItem);
		}

		bucketView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showList(bucket);
			}
		});
		bucketView.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View view) {
				dragAndDropPlayground.startDrag(view, DragAndDropPlayground.DraggedSource.FROM_BUCKET, bucket);
				return false;
			}
		});

		buckets.add(bucket);
	}

	private void showList(Bucket bucket) {
		for (Bucket bucketIter : buckets) {
			if (bucketIter == bucket) {
				bucketIter.getContainer().setVisibility(View.VISIBLE);
				bucketIter.setActive(true);
			} else {
				bucketIter.getContainer().setVisibility(View.GONE);
				bucketIter.setActive(false);
			}
		}
	}

	private void removeBucket(Bucket bucket) {
		boolean removedActive = bucket.isActive();
		bucketsViewGroup.removeView(bucket.getBucketView());
		listsItemsContainer.removeView(bucket.getContainer());
		buckets.remove(bucket);
		if (removedActive && !buckets.isEmpty()) {
			showList(buckets.get(0));
		}
	}

	private void addItemToBucket(Bucket bucket, final BucketItem bucketItem) {
		bucketItem.setBucket(bucket);
		bucket.getItems().add(bucketItem);
		bucketItem.getView().setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View view) {
				dragAndDropPlayground.startDrag(view, DragAndDropPlayground.DraggedSource.FROM_LIST, bucketItem);
				return false;
			}
		});
	}

	private void removeItemfromBucket(BucketItem bucketItem) {
		bucketItem.getBucket().getList().removeView(bucketItem.getView());
		bucketItem.getBucket().getItems().remove(bucketItem);
	}
}
