package jiyang.cdu.kits.model.enty;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Table("fav_book")
public class FavBook {
    @PrimaryKey(AssignType.AUTO_INCREMENT)
    public int id;

    @NonNull
    public String created;

    @NonNull
    public String bookName;

    @Nullable
    public String bookCode;

    @Nullable
    public String bookUrl;

    public FavBook(@NonNull String bookName, String bookCode, String bookUrl) {
        SimpleDateFormat spd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        this.created = spd.format(new Date());
        this.bookName = bookName;
        this.bookCode = bookCode;
        this.bookUrl = bookUrl;
    }
}
