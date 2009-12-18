(( from = 1));
((batch_size = 1000));
((to = from + batch_size))
 for iter  in `seq 0 287000`;
	        do
		     ((from = $iter * batch_size +1))	
			((to = from + batch_size -1))
			((percent = $iter *100 / 28700))
			echo "$percent % done (iter=$iter) ";
		      `psql -hwww.gisgraphy.com -Upostgres -dgisgraphy3 -c "UPDATE openstreetmap set  textsearchvector=to_tsvector('simple',coalesce(name,'')) where name is not null and gid >= $from and gid < $to  " 2>&1 >/dev/null`
done