#!/bin/bash

echo "Đang tạo dữ liệu test (Candidates & Jobs)..."

API_GATEWAY="http://localhost:8080"

# 1. Tạo 5 Candidates
echo "Đang tạo Candidates..."
CANDIDATE_IDS=()

declare -a CANDIDATES=(
  '{"fullName": "Nguyen Van A", "email": "nguyenvana@gmail.com", "phone": "0901234567", "address": "Ho Chi Minh"}'
  '{"fullName": "Tran Thi B", "email": "tranthib@yahoo.com", "phone": "0912345678", "address": "Ha Noi"}'
  '{"fullName": "Le Van C", "email": "levanc@outlook.com", "phone": "0923456789", "address": "Da Nang"}'
  '{"fullName": "Pham Thi D", "email": "phamthid@gmail.com", "phone": "0934567890", "address": "Can Tho"}'
  '{"fullName": "Hoang Van E", "email": "hoangvane@gmail.com", "phone": "0945678901", "address": "Hai Phong"}'
)

for i in "${!CANDIDATES[@]}"; do
  RESPONSE=$(curl -s -X POST "$API_GATEWAY/api/v1/candidates" \
    -H "Content-Type: application/json" \
    -d "${CANDIDATES[$i]}")
  
  CANDIDATE_ID=$(echo $RESPONSE | grep -o '"id":"[^"]*' | cut -d'"' -f4)
  if [ -n "$CANDIDATE_ID" ]; then
    echo "Đã tạo Candidate: $CANDIDATE_ID"
    CANDIDATE_IDS+=($CANDIDATE_ID)
  else
    echo "Lỗi tạo Candidate: $RESPONSE"
  fi
done

# 2. Tạo 5 Jobs
echo -e "\n Đang tạo Jobs..."
JOB_IDS=()
# Dummy department UUID
DEP_ID="123e4567-e89b-12d3-a456-426614174000"
NEXT_MONTH=$(date -v+1m +%Y-%m-%d 2>/dev/null || date -d "next month" +%Y-%m-%d)

declare -a JOBS=(
  '{"title": "Senior Java Backend Engineer", "description": "Yêu cầu 5 năm KN Java Spring Boot, Microservices", "departmentId": "'$DEP_ID'", "requiredSkills": ["Java", "Spring Boot", "RabbitMQ"], "minSalary": 30000000, "maxSalary": 50000000, "deadline": "'$NEXT_MONTH'"}'
  '{"title": "Frontend ReactJS Developer", "description": "Tham gia dự án e-commerce lớn, làm việc với ReactJS, Redux", "departmentId": "'$DEP_ID'", "requiredSkills": ["ReactJS", "Redux", "TypeScript"], "minSalary": 20000000, "maxSalary": 35000000, "deadline": "'$NEXT_MONTH'"}'
  '{"title": "DevOps Engineer", "description": "Vận hành hệ thống trên AWS, Kubernetes, CI/CD", "departmentId": "'$DEP_ID'", "requiredSkills": ["AWS", "Kubernetes", "Docker", "Jenkins"], "minSalary": 40000000, "maxSalary": 70000000, "deadline": "'$NEXT_MONTH'"}'
  '{"title": "QA Automation Engineer", "description": "Viết automation test script với Selenium, Cypress", "departmentId": "'$DEP_ID'", "requiredSkills": ["Selenium", "Cypress", "Java"], "minSalary": 15000000, "maxSalary": 25000000, "deadline": "'$NEXT_MONTH'"}'
  '{"title": "Product Owner", "description": "Lên requirement, quản lý backlog, làm việc với đội Dev", "departmentId": "'$DEP_ID'", "requiredSkills": ["Scrum", "Agile", "Jira"], "minSalary": 35000000, "maxSalary": 60000000, "deadline": "'$NEXT_MONTH'"}'
)

for i in "${!JOBS[@]}"; do
  RESPONSE=$(curl -s -X POST "$API_GATEWAY/api/v1/jobs" \
    -H "Content-Type: application/json" \
    -d "${JOBS[$i]}")
  
  JOB_ID=$(echo $RESPONSE | grep -o '"id":"[^"]*' | cut -d'"' -f4)
  if [ -n "$JOB_ID" ]; then
    echo " Đã tạo Job: $JOB_ID"
    JOB_IDS+=($JOB_ID)
  else
    echo " Lỗi tạo Job: $RESPONSE"
  fi
done

# 3. Chuyển status của Jobs sang PUBLISHED trực tiếp bằng DB (do chưa có endpoint Publish)
echo -e "\n Đang publish các Job (cập nhật trực tiếp DB qua container job-db)..."
docker exec job-db psql -U dieunt -d talenthub_job_dbs -c "UPDATE jobs SET status = 'PUBLISHED';"
echo " Đã chuyển toàn bộ Jobs thành PUBLISHED"

# 4. Submit Applications để test RabbitMQ
echo -e "\n Đang nộp đơn (Submit Applications) để trigger RabbitMQ..."
if [ ${#CANDIDATE_IDS[@]} -gt 0 ] && [ ${#JOB_IDS[@]} -gt 0 ]; then
  # Candidate 1 nộp Job 1
  echo " Nộp đơn: Candidate 1 -> Job 1"
  curl -s -X POST "$API_GATEWAY/api/v1/applications" \
    -H "Content-Type: application/json" \
    -d "{\"candidateId\": \"${CANDIDATE_IDS[0]}\", \"jobId\": \"${JOB_IDS[0]}\"}"
  echo ""

  # Candidate 2 nộp Job 1
  echo " Nộp đơn: Candidate 2 -> Job 1"
  curl -s -X POST "$API_GATEWAY/api/v1/applications" \
    -H "Content-Type: application/json" \
    -d "{\"candidateId\": \"${CANDIDATE_IDS[1]}\", \"jobId\": \"${JOB_IDS[0]}\"}"
  echo ""

  # Candidate 3 nộp Job 3
  echo " Nộp đơn: Candidate 3 -> Job 3"
  curl -s -X POST "$API_GATEWAY/api/v1/applications" \
    -H "Content-Type: application/json" \
    -d "{\"candidateId\": \"${CANDIDATE_IDS[2]}\", \"jobId\": \"${JOB_IDS[2]}\"}"
  echo ""
  
  echo -e "\n Hoàn tất! Vui lòng kiểm tra:"
  echo "1. RabbitMQ Management UI (http://localhost:15672) để xem queue."
  echo "2. Mailpit Web UI (http://localhost:8025) để xem email tự động gửi."
else
  echo " Không có đủ Candidate/Job để tạo Application."
fi
