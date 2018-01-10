package searchpp.model.products;

import org.json.simple.JSONObject;

/**
 * Created by Tobi on 04.12.2017.
 */
public enum Condition
{
    //Condition ID from ebay (https://developer.ebay.com/devzone/finding/callref/Enums/conditionIdList.html)
    NEW,   //New
    NEW_OTHER,   //New other
    NEW_DEFECTS,   //New with defects
    MANUFACTURER_REFURBISHED,   //Manufacturer refurbished
    SELLER_REFURBISHED,   //Seller refurbished
    USED,   //Used
    VERY_GOOD,   //Very Good
    GOOD,   //Good
    ACCEPTABLE,   //Acceptable
    DEFECT,    //For parts or not working
    OTHER;

    public static Condition getProductCondition(String cond)
    {
        Condition condition;
        switch(cond)
        {
            case "1000":
            case "New":
                condition = NEW;
                break;
            case "1500":
                condition = NEW_OTHER;
                break;
            case "1750":
                condition = NEW_DEFECTS;
                break;
            case "2000":
                condition = MANUFACTURER_REFURBISHED;
                break;
            case "2500":
                condition = SELLER_REFURBISHED;
                break;
            case "3000":
                condition = USED;
                break;
            case "4000":
                condition = VERY_GOOD;
                break;
            case "5000":
                condition = GOOD;
                break;
            case "6000":
                condition = ACCEPTABLE;
                break;
            case "7000":
                condition = DEFECT;
                break;
            default:
                condition = OTHER;
                break;
        }
        return condition;
    }

}
