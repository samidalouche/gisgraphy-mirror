#clean type
update openstreetmap  set type = regexp_replace(type, '.*:', '') where type like 'plan.at%';

#update type to remove less frequent type
update openstreetmap set type= NULL where type not in (select lessFrequentType.type from (select o.type,count(type) as count from openstreetmap o group by type order by count) as lessFrequentType where lessFrequentType.count < 1000);
update openstreetmap set type= NULL where type= 'unclassified';
#check select count(oneway) as c, oneway from openstreetmap group by oneway order by C desc

#clean oneway 
update openstreetmap set oneway='false' where oneway <> '1' and oneway <> 'yes' and oneway <> 'true' ;
update openstreetmap set oneway='true' where oneway = 'yes' or oneway = '1';
update openstreetmap set oneway='false' where oneway <> 'false' and oneway <> 'true';
update openstreetmap set oneway='false' where oneway is null;

#update MidPoint (location) 
update openstreetmap set location=line_interpolate_point(LineMerge(shape),0.5)