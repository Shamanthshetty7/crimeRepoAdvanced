import React, { useEffect, useState } from 'react';
import Select from 'react-select';

const LocationFilter = ({ context, setLocationFilter }) => {
  const [menuIsOpen, setMenuIsOpen] = useState(false);
  const [locationOptions, setLocationOptions] = useState([]);

  useEffect(() => {
    const newLocationOptions = [
        ...new Map(
          context.allReports?.filter(report=>report.isActive).map((report) => [report.reportLocation, {
            value: report.reportLocation,
            label: report.reportLocation
          }])
        ).values()
      ];
      
    setLocationOptions(newLocationOptions);
  }, [context.allReports]);

  const onValueChange = (selected) => {
    const selectedValue = selected ? selected.value : null;
    setLocationFilter(selectedValue);
  };

  const onInputChange = (inputValue, { action, prevInputValue }) => {
    if (action === 'input-change') return inputValue;
    if (action === 'menu-close') {
      if (prevInputValue) setMenuIsOpen(true);
      else setMenuIsOpen(undefined);
    }
    return prevInputValue;
  };

  return (
    <Select
      isMulti={false}  
      isClearable
      isSearchable
      onInputChange={onInputChange}
      onChange={onValueChange}
      name="location"
      options={locationOptions}
      menuIsOpen={menuIsOpen}
      className="react-select-container w-75"  
      placeholder="Select Location"
    />
  );
};

export default LocationFilter;
