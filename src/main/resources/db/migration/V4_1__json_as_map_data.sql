--
--
--

insert into json_as_map (id, alternate_id, points, metadata, modified_date)
select uuid('cdd7cacd-8e0a-4372-8ceb-' || lpad(seq::text, 12, '0')),
       '' || lpad(seq::text, 10, '0'),
       0,
       '{ "name" : "Jon Snow", "age" : "23", "hair" : "dark brown", "eyes" : "dark grey" }',
       clock_timestamp()
from generate_series(0, ${jsonAsMapCount}) as seq;