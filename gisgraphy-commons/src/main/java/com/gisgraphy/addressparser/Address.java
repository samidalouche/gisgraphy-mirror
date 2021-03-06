package com.gisgraphy.addressparser;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = Constants.ADDRESS_ROOT_JAXB_NAME)
@XmlAccessorType(XmlAccessType.FIELD)
public class Address {

    private Double lng;
    private Double lat;
    private String recipientName;
	
    private String houseNumber;
    /**
     * fractional number, bis, ter
     */
    private String houseNumberInfo;

    private String POBox;
    private String POBoxInfo;
    private String POBoxAgency;

  
    /**
     * for canada, and more
     */
    private String civicNumberSuffix;

    private String preDirection;
    private String streetName;
    private String streetType;
    private String postDirection;
    
    private String preDirectionIntersection;
    private String streetNameIntersection;
    private String StreetTypeIntersection;
    private String postDirectionIntersection;

    private String extraInfo;
    private String floor;
    private String quarter;

    private String zipCode;
    private String district;
    private String dependentLocality;
    private String city;
    private String postTown;
    private String state;

    private String sector;
    private String quadrant;
    private String block;
    

    /**
	 * @return the lat
	 */
	public Double getLat() {
		return lat;
	}

	/**
	 * @param lat the lat to set
	 */
	public void setLat(Double lat) {
		this.lat = lat;
	}

	/**
	 * @return the lng
	 */
	public Double getLng() {
		return lng;
	}

	/**
	 * @param lng the lng to set
	 */
	public void setLng(Double lng) {
		this.lng = lng;
	}

	public String getStreetNameIntersection() {
	return streetNameIntersection;
    }

    public void setStreetNameIntersection(String streetNameIntersection) {
	this.streetNameIntersection = streetNameIntersection;
    }

    public String getStreetTypeIntersection() {
	return StreetTypeIntersection;
    }

    public void setStreetTypeIntersection(String streetTypeIntersection) {
	StreetTypeIntersection = streetTypeIntersection;
    }

    private String PostOfficeBox;

    public String getStreetType() {
	return streetType;
    }

    public void setStreetType(String streetType) {
	this.streetType = streetType;
    }

    public String getFloor() {
	return floor;
    }

    public void setFloor(String floor) {
	this.floor = floor;
    }

    public String getExtraInfo() {
	return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
	if (extraInfo!= null && !"".equals(extraInfo.trim())){
	    this.extraInfo = extraInfo;
	}
    }

    public String getHouseNumber() {
	return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
	this.houseNumber = houseNumber;
    }

    public String getStreetName() {
	return streetName;
    }

    public void setStreetName(String streetName) {
	this.streetName = streetName;
    }

    public String getCity() {
	return city;
    }

    public void setCity(String city) {
	this.city = city;
    }

    public String getState() {
	return state;
    }

    public void setState(String state) {
	this.state = state;
    }

    public String getZipCode() {
	return zipCode;
    }

    public void setZipCode(String zipCode) {
	this.zipCode = zipCode;
    }

    public String getSector() {
	return sector;
    }

    public void setSector(String sector) {
	this.sector = sector;
    }

    public String getQuadrant() {
	return quadrant;
    }

    public void setQuadrant(String quadrant) {
	this.quadrant = quadrant;
    }

    public String getBlock() {
	return block;
    }

    public void setBlock(String block) {
	this.block = block;
    }


    public String getHouseNumberInfo() {
	return houseNumberInfo;
    }

    public void setHouseNumberInfo(String houseNumberInfo) {
	this.houseNumberInfo = houseNumberInfo;
    }

    public String getPreDirection() {
	return preDirection;
    }

    public void setPredirection(String preDirection) {
	this.preDirection = preDirection;
    }

    public String getPostDirection() {
	return postDirection;
    }

    public void setPostDirection(String postDirection) {
	this.postDirection = postDirection;
    }

    public String getPreDirectionIntersection() {
	return preDirectionIntersection;
    }

    public void setPredirectionIntersection(String preDirectionIntersection) {
	this.preDirectionIntersection = preDirectionIntersection;
    }

    public String getPostDirectionIntersection() {
	return postDirectionIntersection;
    }

    public void setPostDirectionIntersection(String postDirectionIntersection) {
	this.postDirectionIntersection = postDirectionIntersection;
    }


    public String getPOBox() {
	return POBox;
    }

    public void setPOBox(String pOBox) {
	POBox = pOBox;
    }

    public String getCivicNumberSuffix() {
	return civicNumberSuffix;
    }

    public void setCivicNumberSuffix(String civicNumberSuffix) {
	this.civicNumberSuffix = civicNumberSuffix;
    }

	public String getPOBoxInfo() {
		return POBoxInfo;
	}

	public void setPOBoxInfo(String boxInfo) {
		this.POBoxInfo = boxInfo;
	}

	public void setPreDirection(String preDirection) {
		this.preDirection = preDirection;
	}

	/**
	 * @param preDirectionIntersection the preDirectionIntersection to set
	 */
	public void setPreDirectionIntersection(String preDirectionIntersection) {
		this.preDirectionIntersection = preDirectionIntersection;
	}

	/**
	 * @return the recipientName
	 */
	public String getRecipientName() {
		return recipientName;
	}

	public void setRecipientName(String recipientName) {
		this.recipientName = recipientName;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getPostTown() {
	    return postTown;
	}

	public void setPostTown(String postTown) {
	    this.postTown = postTown;
	}

	public String getDependentLocality() {
		return dependentLocality;
	}

	public void setDependentLocality(String dependentLocality) {
		this.dependentLocality = dependentLocality;
	}

	public void setPOBoxAgency(String POBoxAgency) {
		this.POBoxAgency=POBoxAgency;
		
	}

	public String getPOBoxAgency() {
		return POBoxAgency;
	}
	
	public String getQuarter() {
	    return quarter;
	}

	public void setQuarter(String quarter) {
	    this.quarter = quarter;
	}

	public String getPostOfficeBox() {
	    return PostOfficeBox;
	}

	public void setPostOfficeBox(String postOfficeBox) {
	    PostOfficeBox = postOfficeBox;
	}

	@Override
	public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + ((POBox == null) ? 0 : POBox.hashCode());
	    result = prime * result + ((POBoxAgency == null) ? 0 : POBoxAgency.hashCode());
	    result = prime * result + ((POBoxInfo == null) ? 0 : POBoxInfo.hashCode());
	    result = prime * result + ((PostOfficeBox == null) ? 0 : PostOfficeBox.hashCode());
	    result = prime * result + ((StreetTypeIntersection == null) ? 0 : StreetTypeIntersection.hashCode());
	    result = prime * result + ((block == null) ? 0 : block.hashCode());
	    result = prime * result + ((city == null) ? 0 : city.hashCode());
	    result = prime * result + ((civicNumberSuffix == null) ? 0 : civicNumberSuffix.hashCode());
	    result = prime * result + ((dependentLocality == null) ? 0 : dependentLocality.hashCode());
	    result = prime * result + ((district == null) ? 0 : district.hashCode());
	    result = prime * result + ((extraInfo == null) ? 0 : extraInfo.hashCode());
	    result = prime * result + ((floor == null) ? 0 : floor.hashCode());
	    result = prime * result + ((houseNumber == null) ? 0 : houseNumber.hashCode());
	    result = prime * result + ((houseNumberInfo == null) ? 0 : houseNumberInfo.hashCode());
	    result = prime * result + ((postDirection == null) ? 0 : postDirection.hashCode());
	    result = prime * result + ((postDirectionIntersection == null) ? 0 : postDirectionIntersection.hashCode());
	    result = prime * result + ((postTown == null) ? 0 : postTown.hashCode());
	    result = prime * result + ((preDirection == null) ? 0 : preDirection.hashCode());
	    result = prime * result + ((preDirectionIntersection == null) ? 0 : preDirectionIntersection.hashCode());
	    result = prime * result + ((quadrant == null) ? 0 : quadrant.hashCode());
	    result = prime * result + ((quarter == null) ? 0 : quarter.hashCode());
	    result = prime * result + ((recipientName == null) ? 0 : recipientName.hashCode());
	    result = prime * result + ((sector == null) ? 0 : sector.hashCode());
	    result = prime * result + ((state == null) ? 0 : state.hashCode());
	    result = prime * result + ((streetName == null) ? 0 : streetName.hashCode());
	    result = prime * result + ((streetNameIntersection == null) ? 0 : streetNameIntersection.hashCode());
	    result = prime * result + ((streetType == null) ? 0 : streetType.hashCode());
	    result = prime * result + ((zipCode == null) ? 0 : zipCode.hashCode());
	    return result;
	}

	@Override
	public boolean equals(Object obj) {
	    if (this == obj)
		return true;
	    if (obj == null)
		return false;
	    if (getClass() != obj.getClass())
		return false;
	    Address other = (Address) obj;
	    if (POBox == null) {
		if (other.POBox != null)
		    return false;
	    } else if (!POBox.equals(other.POBox))
		return false;
	    if (POBoxAgency == null) {
		if (other.POBoxAgency != null)
		    return false;
	    } else if (!POBoxAgency.equals(other.POBoxAgency))
		return false;
	    if (POBoxInfo == null) {
		if (other.POBoxInfo != null)
		    return false;
	    } else if (!POBoxInfo.equals(other.POBoxInfo))
		return false;
	    if (PostOfficeBox == null) {
		if (other.PostOfficeBox != null)
		    return false;
	    } else if (!PostOfficeBox.equals(other.PostOfficeBox))
		return false;
	    if (StreetTypeIntersection == null) {
		if (other.StreetTypeIntersection != null)
		    return false;
	    } else if (!StreetTypeIntersection.equals(other.StreetTypeIntersection))
		return false;
	    if (block == null) {
		if (other.block != null)
		    return false;
	    } else if (!block.equals(other.block))
		return false;
	    if (city == null) {
		if (other.city != null)
		    return false;
	    } else if (!city.equals(other.city))
		return false;
	    if (civicNumberSuffix == null) {
		if (other.civicNumberSuffix != null)
		    return false;
	    } else if (!civicNumberSuffix.equals(other.civicNumberSuffix))
		return false;
	    if (dependentLocality == null) {
		if (other.dependentLocality != null)
		    return false;
	    } else if (!dependentLocality.equals(other.dependentLocality))
		return false;
	    if (district == null) {
		if (other.district != null)
		    return false;
	    } else if (!district.equals(other.district))
		return false;
	    if (extraInfo == null) {
		if (other.extraInfo != null)
		    return false;
	    } else if (!extraInfo.equals(other.extraInfo))
		return false;
	    if (floor == null) {
		if (other.floor != null)
		    return false;
	    } else if (!floor.equals(other.floor))
		return false;
	    if (houseNumber == null) {
		if (other.houseNumber != null)
		    return false;
	    } else if (!houseNumber.equals(other.houseNumber))
		return false;
	    if (houseNumberInfo == null) {
		if (other.houseNumberInfo != null)
		    return false;
	    } else if (!houseNumberInfo.equals(other.houseNumberInfo))
		return false;
	    if (postDirection == null) {
		if (other.postDirection != null)
		    return false;
	    } else if (!postDirection.equals(other.postDirection))
		return false;
	    if (postDirectionIntersection == null) {
		if (other.postDirectionIntersection != null)
		    return false;
	    } else if (!postDirectionIntersection.equals(other.postDirectionIntersection))
		return false;
	    if (postTown == null) {
		if (other.postTown != null)
		    return false;
	    } else if (!postTown.equals(other.postTown))
		return false;
	    if (preDirection == null) {
		if (other.preDirection != null)
		    return false;
	    } else if (!preDirection.equals(other.preDirection))
		return false;
	    if (preDirectionIntersection == null) {
		if (other.preDirectionIntersection != null)
		    return false;
	    } else if (!preDirectionIntersection.equals(other.preDirectionIntersection))
		return false;
	    if (quadrant == null) {
		if (other.quadrant != null)
		    return false;
	    } else if (!quadrant.equals(other.quadrant))
		return false;
	    if (quarter == null) {
		if (other.quarter != null)
		    return false;
	    } else if (!quarter.equals(other.quarter))
		return false;
	    if (recipientName == null) {
		if (other.recipientName != null)
		    return false;
	    } else if (!recipientName.equals(other.recipientName))
		return false;
	    if (sector == null) {
		if (other.sector != null)
		    return false;
	    } else if (!sector.equals(other.sector))
		return false;
	    if (state == null) {
		if (other.state != null)
		    return false;
	    } else if (!state.equals(other.state))
		return false;
	    if (streetName == null) {
		if (other.streetName != null)
		    return false;
	    } else if (!streetName.equals(other.streetName))
		return false;
	    if (streetNameIntersection == null) {
		if (other.streetNameIntersection != null)
		    return false;
	    } else if (!streetNameIntersection.equals(other.streetNameIntersection))
		return false;
	    if (streetType == null) {
		if (other.streetType != null)
		    return false;
	    } else if (!streetType.equals(other.streetType))
		return false;
	    if (zipCode == null) {
		if (other.zipCode != null)
		    return false;
	    } else if (!zipCode.equals(other.zipCode))
		return false;
	    return true;
	}

	



}
