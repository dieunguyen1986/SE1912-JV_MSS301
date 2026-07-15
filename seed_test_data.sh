#!/bin/bash
set -e

# Helper: chạy query và chỉ lấy đúng UUID trả về, bỏ qua mọi dòng khác (vd "INSERT 0 1")
UUID_REGEX='[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}'
run_query_get_uuid() {
  local container=$1
  local user=$2
  local dbname=$3
  local sql=$4
  docker exec -i "$container" psql -U "$user" -d "$dbname" -t -A -c "$sql" | grep -Eo "$UUID_REGEX" | head -n1
}

echo "=== Seeding test data trực tiếp vào DB (bypass API Gateway) ==="

# ---------- JOB SERVICE ----------
JOB_DB="talenthub-job-db-1"
JOB_USER="dieunt"
JOB_DBNAME="talenthub_job_dbs"

echo -e "\n[1/6] Tạo Department..."
DEPT_ID=$(run_query_get_uuid $JOB_DB $JOB_USER $JOB_DBNAME "
INSERT INTO departments (id, created_at, is_deleted, name, manager_id)
VALUES (gen_random_uuid(), now(), false, 'Engineering', gen_random_uuid())
RETURNING id;
")
echo "Department ID: $DEPT_ID"

echo -e "\n[2/6] Tạo Skills..."
SKILL_JAVA=$(run_query_get_uuid $JOB_DB $JOB_USER $JOB_DBNAME "
INSERT INTO skills (id, created_at, is_deleted, name, type)
VALUES (gen_random_uuid(), now(), false, 'Java', 'TECHNICAL')
RETURNING id;
")
SKILL_REACT=$(run_query_get_uuid $JOB_DB $JOB_USER $JOB_DBNAME "
INSERT INTO skills (id, created_at, is_deleted, name, type)
VALUES (gen_random_uuid(), now(), false, 'ReactJS', 'TECHNICAL')
RETURNING id;
")
echo "Skill Java: $SKILL_JAVA | Skill React: $SKILL_REACT"

echo -e "\n[3/6] Tạo Jobs..."
JOB1_ID=$(run_query_get_uuid $JOB_DB $JOB_USER $JOB_DBNAME "
INSERT INTO jobs (id, created_at, is_deleted, title, description, department_id, min_salary, max_salary, deadline, status)
VALUES (gen_random_uuid(), now(), false, 'Senior Java Backend Engineer', 'Yeu cau 5 nam KN Java Spring Boot', '$DEPT_ID', 30000000, 50000000, now() + interval '30 days', 'PUBLISHED')
RETURNING id;
")
JOB2_ID=$(run_query_get_uuid $JOB_DB $JOB_USER $JOB_DBNAME "
INSERT INTO jobs (id, created_at, is_deleted, title, description, department_id, min_salary, max_salary, deadline, status)
VALUES (gen_random_uuid(), now(), false, 'Frontend ReactJS Developer', 'Du an e-commerce lon', '$DEPT_ID', 20000000, 35000000, now() + interval '30 days', 'PUBLISHED')
RETURNING id;
")
JOB3_ID=$(run_query_get_uuid $JOB_DB $JOB_USER $JOB_DBNAME "
INSERT INTO jobs (id, created_at, is_deleted, title, description, department_id, min_salary, max_salary, deadline, status)
VALUES (gen_random_uuid(), now(), false, 'DevOps Engineer', 'Van hanh he thong AWS, Kubernetes', '$DEPT_ID', 40000000, 70000000, now() + interval '30 days', 'PUBLISHED')
RETURNING id;
")
echo "Job1: $JOB1_ID | Job2: $JOB2_ID | Job3: $JOB3_ID"

for v in DEPT_ID SKILL_JAVA SKILL_REACT JOB1_ID JOB2_ID JOB3_ID; do
  if [ -z "${!v}" ]; then
    echo "LỖI: biến $v rỗng — insert trước đó thất bại. Dừng script."
    exit 1
  fi
done

echo -e "\n[4/6] Gán Skill cho Job..."
docker exec -i $JOB_DB psql -U $JOB_USER -d $JOB_DBNAME -c "
INSERT INTO job_skills (id, created_at, is_deleted, job_id, skill_id, required_level, is_mandatory)
VALUES
  (gen_random_uuid(), now(), false, '$JOB1_ID', '$SKILL_JAVA', 'ADVANCED', true),
  (gen_random_uuid(), now(), false, '$JOB2_ID', '$SKILL_REACT', 'ADVANCED', true);
"

# ---------- CANDIDATE SERVICE ----------
CAND_DB="talenthub-candidate-db-1"
CAND_USER="dieunt"
CAND_DBNAME="talenthub_candidate_dbs"

echo -e "\n[5/6] Tạo Candidates..."
CAND1_ID=$(run_query_get_uuid $CAND_DB $CAND_USER $CAND_DBNAME "
INSERT INTO candidates (id, created_at, is_deleted, full_name, email, phone, address)
VALUES (gen_random_uuid(), now(), false, 'Nguyen Van A', 'nguyenvana@gmail.com', '0901234567', 'Ho Chi Minh')
RETURNING id;
")
CAND2_ID=$(run_query_get_uuid $CAND_DB $CAND_USER $CAND_DBNAME "
INSERT INTO candidates (id, created_at, is_deleted, full_name, email, phone, address)
VALUES (gen_random_uuid(), now(), false, 'Tran Thi B', 'tranthib@yahoo.com', '0912345678', 'Ha Noi')
RETURNING id;
")
CAND3_ID=$(run_query_get_uuid $CAND_DB $CAND_USER $CAND_DBNAME "
INSERT INTO candidates (id, created_at, is_deleted, full_name, email, phone, address)
VALUES (gen_random_uuid(), now(), false, 'Le Van C', 'levanc@outlook.com', '0923456789', 'Da Nang')
RETURNING id;
")
echo "Candidate1: $CAND1_ID | Candidate2: $CAND2_ID | Candidate3: $CAND3_ID"

# ---------- APPLICATION SERVICE ----------
APP_DB="talenthub-application-db-1"
APP_USER="dieunt"
APP_DBNAME="talenthub_application_dbs"

for v in CAND1_ID CAND2_ID CAND3_ID; do
  if [ -z "${!v}" ]; then
    echo "LỖI: biến $v rỗng — insert Candidate thất bại. Dừng script."
    exit 1
  fi
done

echo -e "\n[6/6] Tạo Applications..."
docker exec -i $APP_DB psql -U $APP_USER -d $APP_DBNAME -c "
INSERT INTO applications (id, candidate_id, job_id, current_stage, submitted_at)
VALUES
  (gen_random_uuid(), '$CAND1_ID', '$JOB1_ID', 'NEW', now()),
  (gen_random_uuid(), '$CAND2_ID', '$JOB1_ID', 'NEW', now()),
  (gen_random_uuid(), '$CAND3_ID', '$JOB3_ID', 'NEW', now());
"

echo -e "\n=== HOÀN TẤT ==="
echo "Department: $DEPT_ID"
echo "Jobs: $JOB1_ID, $JOB2_ID, $JOB3_ID"
echo "Candidates: $CAND1_ID, $CAND2_ID, $CAND3_ID"
