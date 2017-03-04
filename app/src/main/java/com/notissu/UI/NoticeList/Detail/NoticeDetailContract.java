package com.notissu.UI.NoticeList.Detail;

import android.app.DownloadManager;

import com.notissu.Model.Notice;
import com.notissu.Model.NoticeDetail;

public interface NoticeDetailContract {
    interface View {
        void showNoticeDetail(NoticeDetail noticeDetail);

        void setViews(Notice notice);

        void showAttchedFiles(AttachedFileAdapter attachedFileList);

        void hideAttchedFiles();

        void showDownload(DownloadManager.Request request);

        void showProgress();

        void hideProgress();
    }

    interface Presenter {
        void fetchNoticeDetail();

        void setAttchedFileAdapter(AttachedFileAdapter attachedFileAdapter);

        void onAttachedFileClick(int position);
    }

    interface OnFetchNoticeDetailListener {

        void onFetchNoticeDetail(String response);
    }
}