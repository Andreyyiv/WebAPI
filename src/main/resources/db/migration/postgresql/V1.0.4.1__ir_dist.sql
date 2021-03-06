CREATE TABLE ${ohdsiSchema}.ir_analysis_dist (
  analysis_id int NOT NULL,
  target_id int NOT NULL,
  outcome_id int NOT NULL,
  strata_sequence INT NULL,
  dist_type int NOT NULL,
  total bigint NOT NULL,
  avg_value float NOT NULL,
  std_dev float NOT NULL,
  min_value int NOT NULL,
  p10_value int NOT NULL,
  p25_value int NOT NULL,
  median_value int NOT NULL,
  p75_value int NOT NULL,
  p90_value int NOT NULL,
  max_value int NULL
)
;
