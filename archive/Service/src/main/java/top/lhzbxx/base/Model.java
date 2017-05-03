package top.lhzbxx.base;

import top.lhzbxx.model.Book;
import top.lhzbxx.model.Record;
import top.lhzbxx.model.RecordSheet;

import java.util.List;

/**
 * Author: LuHao
 * Date: 16/5/27
 * Mail: lhzbxx@gmail.com
 */

public interface Model {

    String createUser(String account, String password, String nickname);

    Record createRecord(Record record, String userId);

    Record updateRecord(Record record, String userId);

    Record readRecord(String recordId);

    void deleteRecord(String recordId);

    Book createBook(Book book, String userId);

    Book updateBook(Book book, String userId);

    Book readBook(String bookId);

    List<Book> readBooks(String userId);

    void deleteBook(String bookId);

    RecordSheet createRecordSheetEntry(RecordSheet recordSheet);

    void deleteRecordSheetEntry(String bookId, String recordId);

    String authorizeUser(String userId);

}
