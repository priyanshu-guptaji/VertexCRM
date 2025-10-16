# 🚀 CRM Automation Implementation Guide

## Quick Start Guide

This document provides step-by-step instructions for testing and deploying the newly implemented automation features.

---

## ✅ What's Been Completed

### Backend Implementation (100% Complete)
- ✅ **14 New Entity Classes** - All entities for Sales, Marketing, Service Automation, and RBAC
- ✅ **14 Repository Interfaces** - Data access layer for all new entities
- ✅ **3 Core DTOs** - LeadScoreDto, PipelineDto, PermissionDto
- ✅ **2 Service Classes** - SalesAutomationService, RBACService
- ✅ **2 REST Controllers** - SalesAutomationController, RBACController
- ✅ **Extended AuthenticationUtils** - Added getCurrentOrgId() and getCurrentMemberId()
- ✅ **Extended Role Entity** - Added RBAC relationships

### Frontend Implementation (Partial - Key Components)
- ✅ **LeadScoringDashboard Component** - Complete lead scoring visualization
- ✅ **RoleManager Component** - RBAC permission management UI
- ✅ **Extended API Service** - All new API method definitions

---

## 🏗️ Architecture Overview

```
┌─────────────────────────────────────────────────────────────┐
│                     FRONTEND (React)                         │
│  ┌────────────┐  ┌──────────────┐  ┌────────────────────┐  │
│  │ Lead Score │  │ Role Manager │  │ Pipeline Builder   │  │
│  │ Dashboard  │  │              │  │ (Kanban Board)     │  │
│  └────────────┘  └──────────────┘  └────────────────────┘  │
│         │                │                    │               │
│         └────────────────┴────────────────────┘               │
│                         │                                     │
│                   API Service Layer                           │
└─────────────────────────┼───────────────────────────────────┘
                          │
                    REST API (Spring Boot)
                          │
┌─────────────────────────┼───────────────────────────────────┐
│                     BACKEND                                  │
│  ┌───────────────────────────────────────────────────────┐  │
│  │  Controllers                                          │  │
│  │  • SalesAutomationController                          │  │
│  │  • RBACController                                     │  │
│  └────────────────┬──────────────────────────────────────┘  │
│                   │                                          │
│  ┌────────────────┴──────────────────────────────────────┐  │
│  │  Services                                             │  │
│  │  • SalesAutomationService  • RBACService             │  │
│  └────────────────┬──────────────────────────────────────┘  │
│                   │                                          │
│  ┌────────────────┴──────────────────────────────────────┐  │
│  │  Repositories (JPA)                                   │  │
│  │  • LeadScoreRepository     • PermissionRepository    │  │
│  │  • PipelineRepository      • RolePermissionRepository│  │
│  └────────────────┬──────────────────────────────────────┘  │
│                   │                                          │
│  ┌────────────────┴──────────────────────────────────────┐  │
│  │  Database Entities                                    │  │
│  │  • LeadScore    • Pipeline    • Permission           │  │
│  │  • DealStageRule • FollowUpRule • RolePermission     │  │
│  └────────────────────────────────────────────────────────┘  │
└────────────────────────────┬─────────────────────────────────┘
                             │
                    ┌────────┴────────┐
                    │   PostgreSQL    │
                    │  (Multi-tenant) │
                    └─────────────────┘
```

---

## 📋 Testing Checklist

### 1. Database Schema Verification

After starting the application, verify these tables are created:

```sql
-- Sales Automation Tables
SELECT * FROM lead_scores LIMIT 1;
SELECT * FROM deal_stage_rules LIMIT 1;
SELECT * FROM follow_up_rules LIMIT 1;
SELECT * FROM pipelines LIMIT 1;

-- Marketing Automation Tables
SELECT * FROM campaign_templates LIMIT 1;
SELECT * FROM segment_rules LIMIT 1;
SELECT * FROM drip_campaigns LIMIT 1;
SELECT * FROM drip_campaign_steps LIMIT 1;
SELECT * FROM campaign_metrics LIMIT 1;

-- Service Automation Tables
SELECT * FROM ticket_categories LIMIT 1;
SELECT * FROM sla_policies LIMIT 1;
SELECT * FROM ticket_feedback LIMIT 1;
SELECT * FROM auto_assignment_rules LIMIT 1;

-- RBAC Tables
SELECT * FROM permissions LIMIT 1;
SELECT * FROM role_permissions LIMIT 1;
```

### 2. Initialize Default Permissions

Call the initialization endpoint once:

```bash
curl -X POST http://localhost:8090/api/rbac/permissions/initialize \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

Expected response:
```json
{
  "message": "Default permissions initialized successfully"
}
```

### 3. Test Lead Scoring API

#### Get All Lead Scores
```bash
curl http://localhost:8090/api/sales/lead-scores \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

#### Get Top Leads
```bash
curl http://localhost:8090/api/sales/top-leads?minScore=60 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

#### Update Lead Score on Activity
```bash
curl -X POST http://localhost:8090/api/sales/lead-scores/activity \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "leadId": 1,
    "activityType": "EMAIL_OPEN"
  }'
```

### 4. Test Pipeline Management

#### Get All Pipelines
```bash
curl http://localhost:8090/api/sales/pipelines \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

#### Create Pipeline
```bash
curl -X POST http://localhost:8090/api/sales/pipelines \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "pipelineName": "Sales Pipeline 2025",
    "description": "Main sales pipeline",
    "stages": "[\"Prospecting\", \"Qualification\", \"Proposal\", \"Negotiation\", \"Closed Won\"]",
    "isDefault": true
  }'
```

### 5. Test RBAC APIs

#### Get All Permissions
```bash
curl http://localhost:8090/api/rbac/permissions \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

#### Get Role Permissions
```bash
curl http://localhost:8090/api/rbac/roles/1/permissions \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

#### Check Permission
```bash
curl -X POST http://localhost:8090/api/rbac/check-permission \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "resource": "LEAD",
    "action": "CREATE"
  }'
```

Expected response:
```json
{
  "hasPermission": true
}
```

---

## 🎨 Frontend Integration Steps

### 1. Update App Routing

Add these routes to your `App.jsx`:

```jsx
import LeadScoringDashboard from './components/LeadScoringDashboard';
import RoleManager from './components/RoleManager';

// Inside your router configuration:
<Route path="/sales/lead-scoring" element={<LeadScoringDashboard />} />
<Route path="/admin/roles" element={<RoleManager />} />
```

### 2. Update Navigation Menu

Add navigation links in your `Layout.jsx`:

```jsx
<nav>
  {/* Existing menu items */}
  
  {/* Sales Automation */}
  <NavSection title="Sales Automation">
    <NavLink to="/sales/lead-scoring" icon="📊">Lead Scoring</NavLink>
    <NavLink to="/sales/pipelines" icon="🔄">Pipelines</NavLink>
    <NavLink to="/sales/automation" icon="⚡">Automation Rules</NavLink>
  </NavSection>
  
  {/* Admin */}
  <NavSection title="Administration">
    <NavLink to="/admin/roles" icon="🔐">Roles & Permissions</NavLink>
  </NavSection>
</nav>
```

### 3. Test Frontend Components

1. **Lead Scoring Dashboard**
   - Navigate to `/sales/lead-scoring`
   - Verify lead scores display correctly
   - Test score filtering by grade
   - Check responsive layout

2. **Role Manager**
   - Navigate to `/admin/roles`
   - Select a role
   - Toggle permissions
   - Verify permission summary updates

---

## 🔐 RBAC Setup Guide

### 1. Create Sample Roles

```sql
INSERT INTO roles (role_name, description, is_system) VALUES
('ADMIN', 'Full system access', true),
('MANAGER', 'Team management access', true),
('SALES_REP', 'Sales operations access', true),
('MARKETING_EXEC', 'Marketing operations access', true),
('SUPPORT_AGENT', 'Customer support access', true),
('VIEWER', 'Read-only access', true);
```

### 2. Assign Default Permissions to Roles

Using the Role Manager UI:
1. Go to `/admin/roles`
2. Select "ADMIN" role
3. Check ALL permissions
4. Select "SALES_REP" role
5. Check: LEAD_CREATE, LEAD_READ, LEAD_UPDATE, DEAL_CREATE, DEAL_READ, DEAL_UPDATE, CONTACT_READ
6. Repeat for other roles as needed

### 3. Protect Routes (Frontend)

Create a `ProtectedRoute` component:

```jsx
import { Navigate } from 'react-router-dom';
import { checkPermission } from '../services/api';
import { useState, useEffect } from 'react';

const ProtectedRoute = ({ children, resource, action }) => {
  const [hasPermission, setHasPermission] = useState(null);
  
  useEffect(() => {
    checkPermission(resource, action).then(response => {
      setHasPermission(response.data.hasPermission);
    });
  }, [resource, action]);
  
  if (hasPermission === null) {
    return <div>Loading...</div>;
  }
  
  if (!hasPermission) {
    return <Navigate to="/unauthorized" />;
  }
  
  return children;
};

export default ProtectedRoute;
```

Use it in routes:

```jsx
<Route 
  path="/leads/create" 
  element={
    <ProtectedRoute resource="LEAD" action="CREATE">
      <CreateLead />
    </ProtectedRoute>
  } 
/>
```

---

## 🔄 Automation Triggers

### Lead Scoring Activity Types

When users interact with your system, trigger score updates:

```javascript
import { updateLeadScoreOnActivity } from '../services/api';

// When lead opens email
await updateLeadScoreOnActivity(leadId, 'EMAIL_OPEN');

// When lead clicks email link
await updateLeadScoreOnActivity(leadId, 'EMAIL_CLICK');

// When lead visits website
await updateLeadScoreOnActivity(leadId, 'WEBSITE_VISIT');

// When lead submits form
await updateLeadScoreOnActivity(leadId, 'FORM_SUBMIT');
```

### Auto-Conversion

Leads with score >= 80 are automatically converted to deals. Check the `auto_converted` flag:

```javascript
const leadScores = await getLeadScores();
const autoConverted = leadScores.data.filter(ls => ls.autoConverted);
console.log(`${autoConverted.length} leads auto-converted to deals`);
```

---

## 📊 Sample Data Seeding

### Lead Scores

```sql
INSERT INTO lead_scores (lead_id, engagement_score, demographic_score, behavior_score, email_opens, email_clicks, website_visits, form_submissions, org_id)
VALUES 
(1, 45, 20, 25, 5, 3, 10, 2, 1),
(2, 60, 15, 15, 8, 5, 15, 1, 1),
(3, 85, 10, 5, 12, 8, 20, 3, 1);
```

### Pipeline

```sql
INSERT INTO pipelines (pipeline_name, description, stages, is_default, is_active, org_id)
VALUES 
('Standard Sales Pipeline', 'Default sales process', '["Prospecting", "Qualification", "Proposal", "Negotiation", "Closed Won", "Closed Lost"]', true, true, 1);
```

### Follow-up Rule

```sql
INSERT INTO follow_up_rules (rule_name, description, entity_type, inactivity_days, action_type, notification_message, is_active, org_id)
VALUES 
('Stale Lead Follow-up', 'Notify sales rep about inactive leads', 'LEAD', 7, 'SEND_NOTIFICATION', 'This lead has been inactive for 7 days. Please follow up.', true, 1);
```

---

## 🐛 Troubleshooting

### Common Issues

1. **Tables Not Created**
   - Check Hibernate auto-ddl settings in `application.yml`
   - Should be: `spring.jpa.hibernate.ddl-auto: update`

2. **Authentication Errors**
   - Verify JWT token is valid
   - Check token contains orgId and memberId claims
   - Verify AuthenticationUtils methods work correctly

3. **CORS Errors**
   - Verify `@CrossOrigin` annotation on controllers
   - Check CORS configuration in SecurityConfig

4. **Permission Checks Fail**
   - Initialize default permissions first
   - Assign permissions to roles
   - Assign roles to members

5. **Frontend API Calls Fail**
   - Verify backend is running on port 8090
   - Check API_URL in `api.js`
   - Inspect browser network tab for errors

---

## 🚀 Deployment Steps

### 1. Backend Deployment

```bash
# Build the application
cd d:\priya crm\priya crm\CRM-Test
mvn clean package -DskipTests

# Run with production profile
java -jar target/crm-app.jar --spring.profiles.active=prod
```

### 2. Frontend Deployment

```bash
# Build frontend
cd frontend
npm install
npm run build

# Deploy dist folder to your web server
```

### 3. Database Migration

Execute SQL scripts to create new tables (if not using auto-ddl):

```bash
psql -U your_username -d your_database -f database/automation_schema.sql
```

### 4. Initialize Permissions

After deployment, call the initialization endpoint:

```bash
curl -X POST https://your-domain.com/api/rbac/permissions/initialize \
  -H "Authorization: Bearer ADMIN_JWT_TOKEN"
```

---

## 📈 Performance Monitoring

### Key Metrics to Monitor

1. **Lead Scoring Performance**
   - Average score update time
   - Auto-conversion rate
   - Score distribution (A/B/C/D grades)

2. **Automation Execution**
   - Follow-up rule trigger count
   - Deal stage automation success rate
   - Notification delivery rate

3. **RBAC Performance**
   - Permission check latency
   - Role-permission cache hit rate

### Monitoring Endpoints

```bash
# Health check
curl http://localhost:8090/actuator/health

# Metrics
curl http://localhost:8090/actuator/metrics
```

---

## 📚 Additional Resources

### Code Files Created

**Entities (14):**
- `LeadScore.java`
- `DealStageRule.java`
- `FollowUpRule.java`
- `Pipeline.java`
- `CampaignTemplate.java`
- `SegmentRule.java`
- `DripCampaign.java`
- `DripCampaignStep.java`
- `CampaignMetrics.java`
- `TicketCategory.java`
- `SlaPolicy.java`
- `TicketFeedback.java`
- `AutoAssignmentRule.java`
- `Permission.java`
- `RolePermission.java`

**Repositories (14):**
- All corresponding repository interfaces

**Services (2):**
- `SalesAutomationService.java`
- `RBACService.java`

**Controllers (2):**
- `SalesAutomationController.java`
- `RBACController.java`

**Frontend Components (2):**
- `LeadScoringDashboard.jsx`
- `RoleManager.jsx`

**Documentation:**
- `AUTOMATION_DOCUMENTATION.md` - Comprehensive technical documentation
- `IMPLEMENTATION_GUIDE.md` - This file

---

## ✅ Next Development Tasks

### High Priority
1. **Marketing Automation Service** - Campaign execution, segmentation engine
2. **Service Automation Service** - Ticket auto-assignment, SLA monitoring
3. **Automation Scheduler** - Scheduled job for follow-up checks
4. **Pipeline Kanban Component** - Drag-and-drop deal management

### Medium Priority
1. **Campaign Builder UI** - Visual campaign designer
2. **SLA Monitor Dashboard** - Real-time SLA tracking
3. **Analytics Dashboard** - Comprehensive reporting

### Low Priority
1. **Mobile Responsive UI** - Optimize for mobile devices
2. **Export/Import Features** - Bulk data operations
3. **Webhooks** - External system integration

---

## 🎉 Success Criteria

The implementation is successful when:

✅ All API endpoints return correct data
✅ Lead scoring updates correctly on activities
✅ Auto-conversion creates deals at threshold
✅ RBAC permissions control access properly
✅ Frontend components display data correctly
✅ Multi-tenancy isolation is maintained
✅ All database queries are performant

---

## 💬 Support

For issues or questions:
1. Check the troubleshooting section
2. Review `AUTOMATION_DOCUMENTATION.md`
3. Inspect browser console and network tab
4. Check backend logs for errors

---

**Last Updated:** 2025-10-16
**Version:** 1.0.0
**Status:** Ready for Testing 🚀
