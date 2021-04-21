package model;

import java.io.Serializable;
/**
 * @author Michael Chiang (mtc166)
 * @author Faizan Dalal (fsd15)
 */

public class Tag implements Serializable{
		private static final long serialVersionUID = 1L;
		private String tagType;
	    private String tagValue;
	    

	    /**
		 * Tag Constructor
		 * @param type	
		 * @param value
		 */

	    public Tag(String type, String value)
	    {
	        this.tagType = type;
	        this.tagValue = value;
	    }

	    /**
		 * equals method 
		 * @return true if the object is equal with the tag, else false
		 */

	    public boolean equals(Object o) {
	        if (o == null || !(o instanceof Tag)) {
	            return false;
	        }

	        Tag compareTag = (Tag) o;

	        if (compareTag.getTagType().equals(this.tagType)) {
	            if (compareTag.getTagValue().equals(this.tagValue)) {
	                return true;
	            }
	        }

	        return false;
	    }

	    
	    /**

		 * Returns string for TagType
		 * @return string	
		 */

	    public String getTagType() {
	        return tagType;
	    }

	    /**

		 * Sets the TagType
		 * @param tagType	
		 */

	    public void setTagType(String tagType) {
	        this.tagType = tagType;
	    }

	    /**

		 * Returns string for TagValue
		 * @return string	
		 */

	    public String getTagValue() {
	        return tagValue;
	    }

	    /**

		 * Sets the TagValue
		 * @param tagValue	
		 */
	    public void setTagValue(String tagValue) {
	        this.tagValue = tagValue;
	    }
	}

