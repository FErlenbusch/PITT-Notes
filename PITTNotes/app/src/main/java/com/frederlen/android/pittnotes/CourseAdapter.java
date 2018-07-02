/*
 * Copyright (C) 2016 The Android Open Source Project
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
package com.frederlen.android.pittnotes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.frederlen.android.pittnotes.models.Course;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseAdapterViewHolder> {

    private List<Course> mCourseData;

    private final CourseAdapterOnClickHandler mClickHandler;

    public interface CourseAdapterOnClickHandler {
        void onClick(Course course);
    }

    public CourseAdapter(CourseAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public class CourseAdapterViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        public final TextView mCourseTextView;

        public CourseAdapterViewHolder(View view) {
            super(view);
            mCourseTextView = (TextView) view.findViewById(R.id.tv_course_data);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Course course = mCourseData.get(adapterPosition);
            mClickHandler.onClick(course);
        }
    }


    @Override
    public CourseAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.course_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new CourseAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CourseAdapterViewHolder courseAdapterViewHolder, int position) {
        String courseInfo = mCourseData.get(position).getCourseName();
        courseAdapterViewHolder.mCourseTextView.setText(courseInfo);
    }

    @Override
    public int getItemCount() {
        if (null == mCourseData) return 0;
        return mCourseData.size();
    }

    public void setCourseData(List<Course> courseData) {
        mCourseData = courseData;
        notifyDataSetChanged();
    }
}