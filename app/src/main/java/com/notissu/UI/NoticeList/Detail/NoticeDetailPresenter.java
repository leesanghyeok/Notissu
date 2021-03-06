package com.notissu.UI.NoticeList.Detail;

import android.app.DownloadManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.kakaolink.v2.model.ButtonObject;
import com.kakao.kakaolink.v2.model.ContentObject;
import com.kakao.kakaolink.v2.model.FeedTemplate;
import com.kakao.kakaolink.v2.model.LinkObject;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;
import com.notissu.Model.AttachedFile;
import com.notissu.Model.NoticeDetail;
import com.notissu.Network.NoticeFetcher;
import com.notissu.UI.NoticeList.NoticeListContract;

import java.util.List;

import io.realm.Realm;

public class NoticeDetailPresenter implements NoticeDetailContract.Presenter,
        NoticeDetailContract.OnFetchNoticeDetailListener {
    private static final String TAG = NoticeDetailPresenter.class.getSimpleName();
    private NoticeDetailContract.View mView;
    private AttachedFileAdapterContract.Model mAdapterModel;
    private AttachedFileAdapterContract.View mAdapterView;

    private Bundle mBundle;
    private NoticeDetail mNoticeDetail;

    private Realm mRealm = Realm.getDefaultInstance();

    private int noticeId;

    public NoticeDetailPresenter(Bundle bundle, NoticeDetailContract.View view) {
        this.mView = view;
        this.mBundle = bundle;
        noticeId = mBundle.getInt(NoticeListContract.KEY_NOTICE_ID);
    }

    @Override
    public void fetchNoticeDetail() {
        mView.showProgress();
        mNoticeDetail = mRealm.where(NoticeDetail.class).equalTo("notice_id", noticeId).findFirst();
        if (mNoticeDetail == null) {
            NoticeFetcher fetcher = new NoticeFetcher(this);
            fetcher.fetchNoticeDetail(noticeId);
        } else {
            showNoticeDetail();
        }
    }

    @Override
    public void setAttchedFileAdapter(AttachedFileAdapter attachedFileAdapter) {
        mAdapterModel = attachedFileAdapter;
        mAdapterView = attachedFileAdapter;
        mView.setAttchedFiles(attachedFileAdapter);
    }

    @Override
    public void onAttachedFileClick(int position) {
        AttachedFile attachedFile = mAdapterModel.getItem(position);
        Uri urlToDownload = Uri.parse(attachedFile.getUrl());
        List<String> pathSegments = urlToDownload.getPathSegments();
        // FIXME: 2017-01-19 다운로드 관련 클래스 생기면 빠질 부분
        DownloadManager.Request request = new DownloadManager.Request(urlToDownload);
        request.setTitle(attachedFile.getTitle());
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, attachedFile.getTitle());
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdirs();
        mView.showDownload(request);
    }

    @Override
    public void setShareNotice() {
        FeedTemplate params = FeedTemplate
                .newBuilder(ContentObject.newBuilder(mNoticeDetail.getTitle(),
                        "https://lh3.googleusercontent.com/r4AnCey_J8IFlnGAjlR9ni2Kx2FGUjiJUwxlWb1bKmsujXHPAYwQyo3-y1EyVItKyPHm=w300-rw",
                        LinkObject.newBuilder().setWebUrl(mNoticeDetail.getUrl())
                                .setMobileWebUrl(mNoticeDetail.getUrl()).build())
                        .setDescrption(mNoticeDetail.getDate())
                        .build())
                .addButton(new ButtonObject("웹에서 보기", LinkObject.newBuilder().setWebUrl(mNoticeDetail.getUrl()).setMobileWebUrl(mNoticeDetail.getUrl()).build()))
                .build();
        mView.showShareNotice(params);

    }

    @Override
    public void onFetchNoticeDetail(String response) {
        mNoticeDetail = NoticeDetail.fromJson(response);
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(mNoticeDetail);
            }
        });

        showNoticeDetail();
    }

    private void showNoticeDetail() {
        mAdapterModel.setAttachedFileList(mNoticeDetail.getAttachedFileList());
        mAdapterView.refresh();

        if (mAdapterModel.getCount() != 0) {
            mView.showFolder();
        } else {
            mView.hideFolder();
        }
        mView.showNoticeDetail(mNoticeDetail);
        mView.hideProgress();
    }
}
