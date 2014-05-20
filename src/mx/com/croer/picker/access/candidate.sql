           select *
	   from ProductoBusq
	   where idItem in (
	    select idItem
                  from (
                  select distinct a.ortograma, o.idItem,  frecuencia
                  from _simigrama as s inner join _alineacion as a 
                                                    on a.simigrama = s.simigrama
                                                    inner join Producto_Ortograma as o
                                                    on o.ortograma = a.ortograma
                  where s.numegrama like '%3733%'
                  ) as v1
             group by idItem
             having sum(frecuencia) >= 1) 
           order by prioridad desc, idItem asc ;
