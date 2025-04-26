A comprehensive task management system with user support, timezone awareness, and robust API endpoints for managing both users and tasks.

Task Management
Create, read, update, and delete tasks
Mandatory fields: Title, Created By (user)
Status-based requirements:
"In Progress" tasks require assigned user
Timezone-aware timestamps
Soft delete functionality
Advanced Features
Timezone conversion for task timestamps
User assignment tracking
Data validation at API level
Proper JPA relationships between entities

Build and run:
mvn clean install
mvn spring-boot:run

Create Task:
POST /api/tasks
{
    "title": "Complete API docs",
    "description": "Document all endpoints",
    "status": "IN_PROGRESS",
    "createdBy": {"id": 1},
    "assignedTo": {"id": 2}
}

Task:
Title: required
CreatedBy: required (user ID)
AssignedTo: required when status is IN_PROGRESS
Status: must be one of: PENDING, IN_PROGRESS, COMPLETED
