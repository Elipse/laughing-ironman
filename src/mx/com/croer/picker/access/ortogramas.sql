            select '<numegrama>' dd, s.numegrama, s.simigrama, a.ortograma, a.alineacion 
              from _simigrama as s inner join _alineacion as a 
                                 on         s.simigrama = a.simigrama 
                                   inner join Producto_Ortograma as o
                                 on         a.ortograma = o.ortograma
             where s.numegrama like '%62%';
