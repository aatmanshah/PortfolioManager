import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "portfolio")
public class PortfolioObj {

    @DatabaseField(id = true)
    String ticker;

    @DatabaseField
    int numShares;

    @DatabaseField
    int initPrice;

    public PortfolioObj(){

    }

    public PortfolioObj(String tick, int n, int i){
        ticker = tick;
        numShares = n;
        initPrice = i;
    }


}
