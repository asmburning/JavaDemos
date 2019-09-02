SELECT  p.CHANNEL_TYPE,p.CHANNEL_CODE,p.SHOW_TYPE, count(p.ID)
FROM posp_rule_channel_priority p
GROUP BY p.CHANNEL_TYPE,p.CHANNEL_CODE,p.SHOW_TYPE
HAVING count(id) > 1;

DELETE FROM posp_rule_channel_priority WHERE
id not in (
	SELECT A.ID FROM(
	SELECT  max(p.ID) as ID
	FROM posp_rule_channel_priority p
	GROUP BY p.CHANNEL_TYPE,p.CHANNEL_CODE,p.SHOW_TYPE
	) A
);

CREATE UNIQUE INDEX rule_channel_priority_idx on  posp_rule_channel_priority(CHANNEL_TYPE,CHANNEL_CODE,SHOW_TYPE);