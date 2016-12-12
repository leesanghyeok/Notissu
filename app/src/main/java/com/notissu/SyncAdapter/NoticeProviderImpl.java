package com.notissu.SyncAdapter;

import android.content.Context;

import com.notissu.Model.RssItem;

import java.util.List;

/**
 * Created by forhack on 2016-12-04.
 */

/*필요할 때 DB연결해서 바로 전달해줘도 되는데 이렇게 한 이유는
* 그냥 순수하게 분리, 그 이유 밖에 없는것 같다.*/

public class NoticeProviderImpl implements NoticeProvider {
    RssDatabase mRssDatabase = RssDatabase.getInstance();

    public NoticeProviderImpl() {}

    @Override
    public List<RssItem> getMainNotice(String category) {
        return mRssDatabase.getMainNotice(category);
    }

    @Override
    public List<RssItem> getLibraryNotice() {
        return mRssDatabase.getLibraryNotice();
    }

    @Override
    public List<RssItem> getStarredNotice() {
        return mRssDatabase.getStarred();
    }

    @Override
    public List<RssItem> getKeywordNotice(String keyword) {
        return mRssDatabase.getKeyword(keyword);
    }

    @Override
    public List<RssItem> getSsuNotice(String category) {
        switch (category) {
            case "전체":
                return getMainNotice(NoticeProvider.NOTICE_SSU_ALL);
            case "학사":
                return getMainNotice(NoticeProvider.NOTICE_SSU_HACKSA);
            case "장학":
                return getMainNotice(NoticeProvider.NOTICE_SSU_JANGHACK);
            case "국제교류":
                return getMainNotice(NoticeProvider.NOTICE_SSU_KUCKJE);
            case "모집,채용":
                return getMainNotice(NoticeProvider.NOTICE_SSU_MOJIP);
            case "교내행사":
                return getMainNotice(NoticeProvider.NOTICE_SSU_KYONE);
            case "교외행사":
                return getMainNotice(NoticeProvider.NOTICE_SSU_KYOWAE);
            case "봉사":
                return getMainNotice(NoticeProvider.NOTICE_SSU_BONGSA);
            default:
                return getMainNotice(NoticeProvider.NOTICE_SSU_ALL);
        }
    }
}
