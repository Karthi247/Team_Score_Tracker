-- Small, repeatable sample dataset for local development.
-- Apply after the application has created the tables in scoretracker_db.

START TRANSACTION;

INSERT IGNORE INTO teams (name, logo_url, description, captain_name, status, created_at) VALUES
('Northside FC', '', 'Fast, attacking football with a strong youth squad.', 'Jordan Lee', 'ACTIVE', CURRENT_TIMESTAMP),
('Harbor United', '', 'A disciplined side built around a solid defense.', 'Avery Chen', 'ACTIVE', CURRENT_TIMESTAMP),
('Summit Athletic', '', 'High-energy pressing and quick transitions.', 'Morgan Patel', 'ACTIVE', CURRENT_TIMESTAMP),
('Riverside Rovers', '', 'A balanced team with a creative midfield.', 'Riley Brooks', 'ACTIVE', CURRENT_TIMESTAMP);

UPDATE teams SET created_at = CURRENT_TIMESTAMP
WHERE name IN ('Northside FC', 'Harbor United', 'Summit Athletic', 'Riverside Rovers')
  AND YEAR(created_at) = 0;

SET @northside_id = (SELECT id FROM teams WHERE name = 'Northside FC' LIMIT 1);
SET @harbor_id = (SELECT id FROM teams WHERE name = 'Harbor United' LIMIT 1);
SET @summit_id = (SELECT id FROM teams WHERE name = 'Summit Athletic' LIMIT 1);
SET @riverside_id = (SELECT id FROM teams WHERE name = 'Riverside Rovers' LIMIT 1);

INSERT IGNORE INTO players (name, jersey_number, age, position, team_id, profile_image_url, status, created_at) VALUES
('Jordan Lee', 9, 24, 'Forward', @northside_id, '', 'ACTIVE', CURRENT_TIMESTAMP),
('Casey Morgan', 1, 27, 'Goalkeeper', @northside_id, '', 'ACTIVE', CURRENT_TIMESTAMP),
('Avery Chen', 10, 25, 'Midfielder', @harbor_id, '', 'ACTIVE', CURRENT_TIMESTAMP),
('Taylor Singh', 4, 26, 'Defender', @harbor_id, '', 'ACTIVE', CURRENT_TIMESTAMP),
('Morgan Patel', 7, 23, 'Winger', @summit_id, '', 'ACTIVE', CURRENT_TIMESTAMP),
('Jamie Flores', 5, 28, 'Defender', @summit_id, '', 'ACTIVE', CURRENT_TIMESTAMP),
('Riley Brooks', 8, 24, 'Midfielder', @riverside_id, '', 'ACTIVE', CURRENT_TIMESTAMP),
('Quinn Davis', 11, 22, 'Forward', @riverside_id, '', 'ACTIVE', CURRENT_TIMESTAMP);

UPDATE players SET created_at = CURRENT_TIMESTAMP
WHERE name IN ('Jordan Lee', 'Casey Morgan', 'Avery Chen', 'Taylor Singh', 'Morgan Patel', 'Jamie Flores', 'Riley Brooks', 'Quinn Davis')
  AND YEAR(created_at) = 0;

INSERT INTO tournaments (name, sport_type, description, start_date, end_date, venue, max_teams, status, format, win_points, draw_points, loss_points, created_at)
SELECT 'City Champions Cup', 'FOOTBALL', 'A four-team local league for the sample season.',
       DATE_SUB(CURDATE(), INTERVAL 7 DAY), DATE_ADD(CURDATE(), INTERVAL 21 DAY),
       'City Stadium', 4, 'ONGOING', 'LEAGUE', 3, 1, 0, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM tournaments WHERE name = 'City Champions Cup');

SET @tournament_id = (SELECT id FROM tournaments WHERE name = 'City Champions Cup' ORDER BY id LIMIT 1);

INSERT IGNORE INTO tournament_teams (tournament_id, team_id, seed_number, registered_at) VALUES
(@tournament_id, @northside_id, 1, CURRENT_TIMESTAMP),
(@tournament_id, @harbor_id, 2, CURRENT_TIMESTAMP),
(@tournament_id, @summit_id, 3, CURRENT_TIMESTAMP),
(@tournament_id, @riverside_id, 4, CURRENT_TIMESTAMP);

INSERT INTO matches (tournament_id, team_a_id, team_b_id, match_date, match_time, venue, status, team_a_score, team_b_score, winner_id, round_name, current_period, elapsed_minutes, created_at, updated_at)
SELECT @tournament_id, @northside_id, @harbor_id, DATE_SUB(CURDATE(), INTERVAL 3 DAY), '16:00:00',
       'City Stadium', 'COMPLETED', 2, 1, @northside_id, 'Matchday 1', 'Full Time', 90, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM matches WHERE tournament_id = @tournament_id AND round_name = 'Matchday 1' AND team_a_id = @northside_id AND team_b_id = @harbor_id);

INSERT INTO matches (tournament_id, team_a_id, team_b_id, match_date, match_time, venue, status, team_a_score, team_b_score, winner_id, round_name, current_period, elapsed_minutes, created_at, updated_at)
SELECT @tournament_id, @summit_id, @riverside_id, DATE_SUB(CURDATE(), INTERVAL 2 DAY), '18:00:00',
       'Riverside Ground', 'COMPLETED', 1, 1, NULL, 'Matchday 1', 'Full Time', 90, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM matches WHERE tournament_id = @tournament_id AND round_name = 'Matchday 1' AND team_a_id = @summit_id AND team_b_id = @riverside_id);

INSERT INTO matches (tournament_id, team_a_id, team_b_id, match_date, match_time, venue, status, team_a_score, team_b_score, winner_id, round_name, current_period, elapsed_minutes, created_at, updated_at)
SELECT @tournament_id, @northside_id, @summit_id, CURDATE(), '17:30:00',
       'City Stadium', 'LIVE', 1, 0, NULL, 'Matchday 2', 'Second Half', 65, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM matches WHERE tournament_id = @tournament_id AND round_name = 'Matchday 2' AND team_a_id = @northside_id AND team_b_id = @summit_id);

INSERT INTO matches (tournament_id, team_a_id, team_b_id, match_date, match_time, venue, status, team_a_score, team_b_score, winner_id, round_name, current_period, elapsed_minutes, created_at, updated_at)
SELECT @tournament_id, @harbor_id, @riverside_id, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '17:00:00',
       'Harbor Field', 'UPCOMING', 0, 0, NULL, 'Matchday 2', 'Scheduled', 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM matches WHERE tournament_id = @tournament_id AND round_name = 'Matchday 2' AND team_a_id = @harbor_id AND team_b_id = @riverside_id);

SET @match_northside_harbor = (SELECT id FROM matches WHERE tournament_id = @tournament_id AND team_a_id = @northside_id AND team_b_id = @harbor_id AND round_name = 'Matchday 1' ORDER BY id LIMIT 1);
SET @match_summit_riverside = (SELECT id FROM matches WHERE tournament_id = @tournament_id AND team_a_id = @summit_id AND team_b_id = @riverside_id AND round_name = 'Matchday 1' ORDER BY id LIMIT 1);
SET @match_northside_summit = (SELECT id FROM matches WHERE tournament_id = @tournament_id AND team_a_id = @northside_id AND team_b_id = @summit_id AND round_name = 'Matchday 2' ORDER BY id LIMIT 1);

INSERT INTO score_events (match_id, team_id, player_id, points, event_type, minute_or_period, description, timestamp)
SELECT @match_northside_harbor, @northside_id, (SELECT id FROM players WHERE team_id = @northside_id AND jersey_number = 9), 1, 'GOAL', '22', 'Jordan Lee opens the scoring.', CURRENT_TIMESTAMP FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM score_events WHERE match_id = @match_northside_harbor AND minute_or_period = '22');
INSERT INTO score_events (match_id, team_id, player_id, points, event_type, minute_or_period, description, timestamp)
SELECT @match_northside_harbor, @harbor_id, (SELECT id FROM players WHERE team_id = @harbor_id AND jersey_number = 10), 1, 'GOAL', '51', 'Avery Chen levels the match.', CURRENT_TIMESTAMP FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM score_events WHERE match_id = @match_northside_harbor AND minute_or_period = '51');
INSERT INTO score_events (match_id, team_id, player_id, points, event_type, minute_or_period, description, timestamp)
SELECT @match_northside_harbor, @northside_id, (SELECT id FROM players WHERE team_id = @northside_id AND jersey_number = 9), 1, 'GOAL', '78', 'Jordan Lee scores the winner.', CURRENT_TIMESTAMP FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM score_events WHERE match_id = @match_northside_harbor AND minute_or_period = '78');
INSERT INTO score_events (match_id, team_id, player_id, points, event_type, minute_or_period, description, timestamp)
SELECT @match_summit_riverside, @summit_id, (SELECT id FROM players WHERE team_id = @summit_id AND jersey_number = 7), 1, 'GOAL', '34', 'Morgan Patel scores for Summit.', CURRENT_TIMESTAMP FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM score_events WHERE match_id = @match_summit_riverside AND minute_or_period = '34');
INSERT INTO score_events (match_id, team_id, player_id, points, event_type, minute_or_period, description, timestamp)
SELECT @match_summit_riverside, @riverside_id, (SELECT id FROM players WHERE team_id = @riverside_id AND jersey_number = 8), 1, 'GOAL', '70', 'Riley Brooks equalizes.', CURRENT_TIMESTAMP FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM score_events WHERE match_id = @match_summit_riverside AND minute_or_period = '70');
INSERT INTO score_events (match_id, team_id, player_id, points, event_type, minute_or_period, description, timestamp)
SELECT @match_northside_summit, @northside_id, (SELECT id FROM players WHERE team_id = @northside_id AND jersey_number = 9), 1, 'GOAL', '53', 'Jordan Lee scores in the second half.', CURRENT_TIMESTAMP FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM score_events WHERE match_id = @match_northside_summit AND minute_or_period = '53');

INSERT INTO team_standings (tournament_id, team_id, matches_played, wins, draws, losses, points_scored, points_conceded, score_difference, tournament_points, position_rank)
SELECT @tournament_id, @northside_id, 1, 1, 0, 0, 2, 1, 1, 3, 1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM team_standings WHERE tournament_id = @tournament_id AND team_id = @northside_id);
INSERT INTO team_standings (tournament_id, team_id, matches_played, wins, draws, losses, points_scored, points_conceded, score_difference, tournament_points, position_rank)
SELECT @tournament_id, @summit_id, 1, 0, 1, 0, 1, 1, 0, 1, 2 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM team_standings WHERE tournament_id = @tournament_id AND team_id = @summit_id);
INSERT INTO team_standings (tournament_id, team_id, matches_played, wins, draws, losses, points_scored, points_conceded, score_difference, tournament_points, position_rank)
SELECT @tournament_id, @riverside_id, 1, 0, 1, 0, 1, 1, 0, 1, 3 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM team_standings WHERE tournament_id = @tournament_id AND team_id = @riverside_id);
INSERT INTO team_standings (tournament_id, team_id, matches_played, wins, draws, losses, points_scored, points_conceded, score_difference, tournament_points, position_rank)
SELECT @tournament_id, @harbor_id, 1, 0, 0, 1, 1, 2, -1, 0, 4 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM team_standings WHERE tournament_id = @tournament_id AND team_id = @harbor_id);

COMMIT;
