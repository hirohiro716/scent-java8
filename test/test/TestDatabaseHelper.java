package test;

import java.sql.SQLException;
import java.util.Date;

import com.hirohiro716.RudeArray;
import com.hirohiro716.database.DataNotFoundException;
import com.hirohiro716.database.mysql.MySQL;

@SuppressWarnings("all")
public class TestDatabaseHelper {

    public static void main(String[] args) {

        RudeArray row;
        try {
            MySQL mysql = new MySQL();
            mysql.connect("localhost", "test", "test", "Hiro6295", "utf8");

            mysql.setAutoCommit(false);

            row = mysql.fetchRow("SELECT * FROM mst_product WHERE product_id = 3 FOR UPDATE;");

            row.put("name", "５００ｇ");
            row.put("major_type_kana", "いくら");
            row.put("major_type_name", "いくら");
            row.put("minor_type_kana", "しおづけ");
            row.put("minor_type_name", "塩漬け");
//            row.set("input_time", new Date());
//            row.set("update_time", new Date());

            mysql.update(row,"SELECT * FROM mst_product WHERE product_id = 3;" );

            mysql.commit();
        } catch (DataNotFoundException exception) {
            return;
        } catch (SQLException exception) {
            return;
        } catch (ClassNotFoundException exception) {
            return;
        }


    }

}
