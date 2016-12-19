package com.notissu.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.notissu.Model.RssItem;

import java.util.ArrayList;
import java.util.List;

import static com.notissu.Model.RssItem.LibraryNotice.TABLE_NAME;

/**
 * Created by forhack on 2016-12-19.
 */
    /*Library 공지사항들의 기능들
    * Library 테이블을 불러오기
    List<RssItem> getNotice()
    * 쓰기
    long addLibrary(RssItem rssItem)
    * 수정하기
    int updateLibrary(RssItem rssItem)
    * 삭제하기.
    int deleteLibrary(RssItem rssItem)
    * */
public class LibraryProviderImp implements LibraryProvider {
    private LowDBProvider mLowDBProvider;
    private SQLiteDatabase mWriteDatabase;

    public LibraryProviderImp() {
        mWriteDatabase = RssDatabase.getInstance().getWriteDatabase();
        mLowDBProvider = new LowDBProviderImp();
    }

    //Library 공지사항 가져오기
    @Override
    public List<RssItem> getNotice() {
        List<RssItem> rssItemList = new ArrayList<>();
        Cursor results = mLowDBProvider.getCursor(RssItem.LibraryNotice.TABLE_NAME,null,null);

        while (!results.isAfterLast()){
            RssItem rssItem = new RssItem(results);
            rssItemList.add(rssItem);
            results.moveToNext();
        };
        results.close();
        return rssItemList;
    }

    //입력받은 RSS를 DB에 삽입하는 메소드
    //실패했을 때 -1 반환
    @Override
    public long addNotice(RssItem isExist) {
        ContentValues values = new ContentValues();

        values.put(RssItem.LibraryNotice.COLUMN_NAME_GUID,isExist.getGuid());
        values.put(RssItem.LibraryNotice.COLUMN_NAME_TITLE,isExist.getTitle());
        values.put(RssItem.LibraryNotice.COLUMN_NAME_LINK,isExist.getLink());
        values.put(RssItem.LibraryNotice.COLUMN_NAME_PUBLISH_DATE,isExist.getPublishDate());
        values.put(RssItem.LibraryNotice.COLUMN_NAME_DESCRIPTION,isExist.getDescription());
        values.put(RssItem.LibraryNotice.COLUMN_NAME_IS_READ,isExist.getIsRead());

        return mWriteDatabase.insert(RssItem.LibraryNotice.TABLE_NAME,null,values);
    }

    //입력받은 RSS를 DB에 업데이트(수정)하는 메소드
    //일치하는 row가 없으면 0 반환
    @Override
    public int updateNotice(RssItem isExist) {
        ContentValues values = new ContentValues();
        values.put(RssItem.LibraryNotice.COLUMN_NAME_GUID,isExist.getGuid());
        values.put(RssItem.LibraryNotice.COLUMN_NAME_LINK,isExist.getLink());
        values.put(RssItem.LibraryNotice.COLUMN_NAME_PUBLISH_DATE,isExist.getPublishDate());
        values.put(RssItem.LibraryNotice.COLUMN_NAME_DESCRIPTION,isExist.getDescription());
        values.put(RssItem.LibraryNotice.COLUMN_NAME_IS_READ,isExist.getIsRead());

        return mWriteDatabase.update(RssItem.LibraryNotice.TABLE_NAME,values,
                RssItem.LibraryNotice.COLUMN_NAME_TITLE+"=?",new String[]{isExist.getTitle()});
    }

    //인자로 넣은 GUID와 일치하는 RSS 삭제
    //일치하는 row가 없으면 0 반환
    @Override
    public int deleteNotice(String title) {
        //두번째 인자를 null로 채우면 모든 row 삭제
        return mWriteDatabase.delete(RssItem.LibraryNotice.TABLE_NAME,
                RssItem.LibraryNotice.COLUMN_NAME_TITLE+"=?",new String[]{title});
    }

    //안 읽은 공지사항의 개수 반환
    @Override
    public int getNotReadCount() {
        int count = 0;
        List<RssItem> rssList = getNotice();;

        for (RssItem rss : rssList) {
            if (rss.getIsRead() == RssItem.NOT_READ) {
                count++;
            }
        }
        return count;
    }

    //안읽은 공지사항 모두 업데이트 하도록 한다.
    @Override
    public int updateAllReadCount() {
        ContentValues values = new ContentValues();
        values.put(RssItem.Common.COLUMN_NAME_IS_READ,RssItem.READ);
        return mWriteDatabase.update(TABLE_NAME,values,null,null);
    }
}
