           select *
	   from ProductoBusq
	   where idBean in (
	    select idBean
                  from (
                  select distinct a.ortograma, o.idBean,  frecuencia
                  from _simigrama as s inner join _alineacion as a 
                                                    on a.simigrama = s.simigrama
                                                    inner join Producto_Ortograma as o
                                                    on o.ortograma = a.ortograma
                  where s.numegrama like '%62%'
                  ) as v1
             group by idBean
             having sum(frecuencia) >= 1) 
           order by prioridad desc, idBean asc ;
