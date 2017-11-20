--step1: save available regions
delete from  shipping_service_available_region
insert into shipping_service_available_region(id,tenant_id,region_node_id) values (1000001,'caas20demo',204),(1000002,'caas20demo',205),(1000003,'caas20demo',206),(1000004,'caas20demo',207),(1000005,'caas20demo',208),(1000006,'caas20demo',209),(1000007,'caas20demo',210),(1000008,'caas20demo',211),(1000009,'caas20demo',212),(1000010,'caas20demo',200),(1000011,'caas20demo',1),(1000012,'caas20demo',6),(1000013,'caas20demo',10)
--step2:create new zone

delete from shipping_service_shipping_zone
insert into shipping_service_shipping_zone(id,tenant_id,delete_flag,name) values (1,'caas20demo',false,'testzone')

--step3: save assigned regions
delete from shipping_service_assigned_region
insert into shipping_service_assigned_region(tenant_id,assigned_level,region_node_id,zone_id) values ('caas20demo',4,204,1),('caas20demo',4,205,1),('caas20demo',4,206,1),('caas20demo',4,207,1)