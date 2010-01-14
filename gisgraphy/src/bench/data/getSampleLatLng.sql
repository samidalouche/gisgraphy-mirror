select replace(replace(astext(location), 'POINT(',''),')','') from gisfeature g where location is not null limit 1000 ;
