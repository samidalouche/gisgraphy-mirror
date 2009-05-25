#clean type
update openstreetmap  set type = regexp_replace(type, '.*:', '') where type like 'plan.at%';
#clean oneway 
update openstreetmap set oneway='false' where oneway <> '1' and oneway <> 'yes' and oneway <> 'true' ;
update openstreetmap set oneway='true' where oneway = 'yes' or oneway = '1';
update openstreetmap set oneway='false' where oneway <> 'false' and oneway <> 'true';
update openstreetmap set oneway='false' where oneway is null;

#check select count(oneway) as c, oneway from openstreetmap group by oneway order by C desc