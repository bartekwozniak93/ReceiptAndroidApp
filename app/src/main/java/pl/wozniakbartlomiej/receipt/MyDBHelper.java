package pl.wozniakbartlomiej.receipt;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yoonsz25 on 10/8/2016.
 */

public class MyDBHelper extends SQLiteOpenHelper {
    public static final int databaseVersion = 1;
    public	static	final	String	databaseName ="ReceiptDB";
    public	static	final	String	tableName =	"Accounts";
    public	static	final	String	columnName1	=	"_id";									//	should	use	_id	as	primary	key
    public	static	final	String	columnName2	=	"password";
    //public	static	final	String	columnName3	=	"name3";
    private	static	final	String	SQLite_CREATE =
            "CREATE	TABLE	"	+	tableName +	"	("	+	columnName1 +	"	VARCHAR(256)," +
                    	columnName2	+	" TEXT	NOT	NULL);";
    private	static	final	String	SQLite_DELETE =
            "DROP	TABLE	IF	EXISTS	"	+	tableName;

    public MyDBHelper(Context context) {
        super(context, databaseName, null, databaseVersion);
    }

    @Override
    public	void	onCreate(SQLiteDatabase db)	{
        db.execSQL(SQLite_CREATE);
    }
    @Override
    public	void	onUpgrade(SQLiteDatabase db,	int oldVersion,	int newVersion)	{
//	note:	our	upgrade	policy	here	is	simply	to	discard	the	data	and	start	all	over
        db.execSQL(SQLite_DELETE);					//	delete	the	existing	database
        onCreate(db);																															//	create	a	new	database
    }
}
