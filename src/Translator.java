public class Translator {
    public String translate(String descriptionText) {
        String editedDescription = descriptionText;
        editedDescription = removeCDATA(editedDescription);
        editedDescription = removeVerbatim(editedDescription);
        return editedDescription;
    } //DONE

    private String removeVerbatim(String editedDescription) {
        editedDescription = editedDescription.replace("\\verbatim", "");
        editedDescription = editedDescription.replace("\\endverbatim", "");
        return editedDescription;
    }

    private String removeCDATA(String editedDescription) {
        editedDescription = editedDescription.replace("<![CDATA[", "");
        editedDescription = editedDescription.replace("]]>", "");
        return editedDescription;
    }
}