public class XMLFormatOrganiser {
    public void /*String[]*/ getTabOrderHierarchy(String totalXMLInput) {
        //Figures out the order in which the tags should be parsed.

        //Page
        //  Top-Description /Top-Description
        //  Options
        //      Option
        //          Option-Description /Option-Description
        //          AcceptableValues /AcceptableValues
        //              Enum /Enum
        //      /Option
        //  /Options
        //  Group
        //      Group-Description /Group-Description
        //      Option /Option
        //      Option /Option
        //      Option /Option
        //  /Group
        ///Page

        //Go through the tags from the Page tag.
        //When a tag opens add it to a list of tags which are 1 level down from the initial Page tag.
        //After this, skip everything until you reach the closing tag for that tag.
        //Continue doing the same thing.
        //When you reach the closing page tag then you have gathered all of the tags which are all 1 level down from the Page tag.
        //For each element in the list of elements that are 1 level down find the tags that are 1 level down from that.
        //Do the same until there are no lower levels. In this example there are 4 levels.
        //Go through all the elements in the lowest level of the structure passing them into the formatter.
        //Pass the output into the formatter as an argument of the function that parses the tag that is 1 level higher than the elements that were just formatted.
        //Continue this until you are at the top level.
        //???
        //Profit.
    }
}