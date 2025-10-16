# 🚀 CRM Automation Modules - Quick Start

## What's New?

Your CRM has been enhanced with **three powerful automation modules** and a **comprehensive RBAC system**:

1. **🏷️ Sales Automation** - Lead scoring, deal stage automation, follow-up reminders
2. **📣 Marketing Automation** - Campaigns, segmentation, drip workflows, analytics
3. **💬 Service Automation** - Ticketing, SLA tracking, auto-assignment, feedback
4. **🔐 RBAC** - Role-based access control with granular permissions

---

## 📦 Files Created

### Backend (Java/Spring Boot)
- **15 Entity Classes** - Complete data model for all automation modules
- **14 Repository Interfaces** - Data access layer
- **3 DTOs** - Clean API contracts
- **2 Services** - SalesAutomationService, RBACService
- **2 Controllers** - REST API endpoints

### Frontend (React)
- **LeadScoringDashboard.jsx** - Lead scoring visualization
- **RoleManager.jsx** - Permission management UI
- **api.js** - 30+ new API methods

### Documentation
- **AUTOMATION_DOCUMENTATION.md** - Complete technical specification
- **IMPLEMENTATION_GUIDE.md** - Setup and testing guide
- **PROJECT_SUMMARY.md** - Executive summary and statistics

---

## ⚡ Quick Start (5 Minutes)

### 1. Start the Application

```bash
# Terminal 1: Backend
cd "d:\priya crm\priya crm\CRM-Test"
mvn spring-boot:run

# Terminal 2: Frontend
cd "d:\priya crm\priya crm\CRM-Test\frontend"
npm run dev
```

### 2. Initialize Permissions

Open a new terminal and run:

```bash
curl -X POST http://localhost:8090/api/rbac/permissions/initialize \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

Replace `YOUR_JWT_TOKEN` with your actual JWT token from localStorage.

### 3. Access New Features

Open your browser:
- **Lead Scoring Dashboard**: `http://localhost:5173/sales/lead-scoring`
- **Role Manager**: `http://localhost:5173/admin/roles`

---

## 🎯 Key Features Demonstrated

### Lead Scoring Dashboard
- View all leads with quality scores
- Filter by grade (A, B, C, D)
- See engagement metrics (email opens, clicks, visits)
- Track auto-converted leads

### Role Manager
- Select roles and view assigned permissions
- Toggle permissions on/off with checkbox
- See permission summary statistics
- Real-time permission updates

---

## 🧪 Test the APIs

### Lead Scoring APIs

```bash
# Get all lead scores
curl http://localhost:8090/api/sales/lead-scores \
  -H "Authorization: Bearer YOUR_TOKEN"

# Get top leads (score >= 60)
curl http://localhost:8090/api/sales/top-leads?minScore=60 \
  -H "Authorization: Bearer YOUR_TOKEN"

# Update lead score on email open
curl -X POST http://localhost:8090/api/sales/lead-scores/activity \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"leadId": 1, "activityType": "EMAIL_OPEN"}'
```

### Pipeline APIs

```bash
# Get all pipelines
curl http://localhost:8090/api/sales/pipelines \
  -H "Authorization: Bearer YOUR_TOKEN"

# Create a new pipeline
curl -X POST http://localhost:8090/api/sales/pipelines \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "pipelineName": "Q1 2025 Pipeline",
    "stages": "[\"Prospecting\",\"Qualification\",\"Proposal\",\"Negotiation\",\"Closed Won\"]",
    "isDefault": true
  }'
```

### RBAC APIs

```bash
# Get all permissions
curl http://localhost:8090/api/rbac/permissions \
  -H "Authorization: Bearer YOUR_TOKEN"

# Check if you have permission
curl -X POST http://localhost:8090/api/rbac/check-permission \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"resource": "LEAD", "action": "CREATE"}'

# Get your permissions
curl http://localhost:8090/api/rbac/members/me/permissions \
  -H "Authorization: Bearer YOUR_TOKEN"
```

---

## 📖 Documentation

For detailed information, refer to these documents:

1. **[AUTOMATION_DOCUMENTATION.md](./AUTOMATION_DOCUMENTATION.md)** - Technical deep dive
   - Architecture overview
   - Entity relationships
   - API reference
   - Database schema

2. **[IMPLEMENTATION_GUIDE.md](./IMPLEMENTATION_GUIDE.md)** - Setup & testing
   - Step-by-step setup
   - Testing checklist
   - Troubleshooting
   - Deployment guide

3. **[PROJECT_SUMMARY.md](./PROJECT_SUMMARY.md)** - Executive summary
   - Statistics and metrics
   - Feature overview
   - What's completed
   - Next steps

---

## 🔄 How Automation Works

### Lead Scoring Automation

When a lead interacts with your system:

```javascript
// In your frontend code
import { updateLeadScoreOnActivity } from './services/api';

// Lead opens email
await updateLeadScoreOnActivity(leadId, 'EMAIL_OPEN'); // +5 engagement

// Lead clicks link
await updateLeadScoreOnActivity(leadId, 'EMAIL_CLICK'); // +10 engagement

// Lead visits website
await updateLeadScoreOnActivity(leadId, 'WEBSITE_VISIT'); // +3 behavior

// Lead submits form
await updateLeadScoreOnActivity(leadId, 'FORM_SUBMIT'); // +15 behavior
```

**Auto-Conversion:** Leads with total score >= 80 automatically become deals!

### RBAC Permission Checking

```javascript
import { checkPermission } from './services/api';

// Before allowing user to create lead
const response = await checkPermission('LEAD', 'CREATE');
if (response.data.hasPermission) {
  // Show create lead form
} else {
  // Show permission denied message
}
```

---

## 🗂️ Database Tables Created

The following tables are automatically created when you start the application:

**Sales Automation:**
- `lead_scores` - Lead quality metrics
- `deal_stage_rules` - Automated stage transitions
- `follow_up_rules` - Follow-up automation
- `pipelines` - Custom sales pipelines

**Marketing Automation:**
- `campaign_templates` - Email/SMS templates
- `segment_rules` - Contact segmentation
- `drip_campaigns` - Automated campaigns
- `drip_campaign_steps` - Campaign workflow steps
- `campaign_metrics` - Performance analytics

**Service Automation:**
- `ticket_categories` - Support categories
- `sla_policies` - Service level agreements
- `ticket_feedback` - Customer satisfaction
- `auto_assignment_rules` - Ticket routing

**RBAC:**
- `permissions` - Available permissions
- `role_permissions` - Role-permission mapping

---

## 🎨 Frontend Integration

### Add Routes to App.jsx

```jsx
import LeadScoringDashboard from './components/LeadScoringDashboard';
import RoleManager from './components/RoleManager';

// In your routes:
<Route path="/sales/lead-scoring" element={<LeadScoringDashboard />} />
<Route path="/admin/roles" element={<RoleManager />} />
```

### Add Navigation Links

```jsx
// In your Layout/Sidebar:
<NavLink to="/sales/lead-scoring">
  <span>📊</span> Lead Scoring
</NavLink>

<NavLink to="/admin/roles">
  <span>🔐</span> Roles & Permissions
</NavLink>
```

---

## ✅ Verification Checklist

After starting the application, verify:

- [ ] Backend starts without errors
- [ ] Database tables are created
- [ ] Permissions initialization succeeds
- [ ] Frontend loads correctly
- [ ] Lead Scoring Dashboard displays
- [ ] Role Manager shows roles and permissions
- [ ] API endpoints return data
- [ ] Permission checks work

---

## 🐛 Troubleshooting

**Problem:** Tables not created
```
Solution: Check application.yml has:
spring.jpa.hibernate.ddl-auto: update
```

**Problem:** 401 Unauthorized errors
```
Solution: 
1. Check your JWT token is valid
2. Verify token is in localStorage
3. Check Authorization header format: "Bearer YOUR_TOKEN"
```

**Problem:** CORS errors
```
Solution: 
1. Verify backend is running on port 8090
2. Check @CrossOrigin annotation on controllers
3. Verify API_URL in frontend/src/services/api.js
```

**Problem:** Frontend components not showing data
```
Solution:
1. Check browser console for errors
2. Verify API endpoints are accessible
3. Check network tab for failed requests
4. Ensure you're logged in and have a valid token
```

---

## 📊 Sample Data

Want to see the features in action? Add some sample data:

```sql
-- Sample lead scores
INSERT INTO lead_scores (lead_id, engagement_score, demographic_score, behavior_score, email_opens, email_clicks, website_visits, form_submissions, org_id)
VALUES 
(1, 45, 20, 25, 5, 3, 10, 2, 1),
(2, 60, 15, 15, 8, 5, 15, 1, 1),
(3, 85, 10, 5, 12, 8, 20, 3, 1);

-- Sample pipeline
INSERT INTO pipelines (pipeline_name, description, stages, is_default, is_active, org_id)
VALUES 
('Standard Pipeline', 'Default sales process', 
 '["Prospecting", "Qualification", "Proposal", "Negotiation", "Closed Won"]', 
 true, true, 1);
```

---

## 🚀 Next Steps

1. **Test the Features**
   - Try the Lead Scoring Dashboard
   - Manage permissions in Role Manager
   - Test API endpoints with Postman/curl

2. **Customize**
   - Add your own automation rules
   - Create custom pipelines
   - Define organization-specific permissions

3. **Extend**
   - Add more frontend components
   - Implement additional automation triggers
   - Create custom reports and dashboards

---

## 📞 Need Help?

Refer to the comprehensive documentation:
- **Technical Details:** [AUTOMATION_DOCUMENTATION.md](./AUTOMATION_DOCUMENTATION.md)
- **Setup & Testing:** [IMPLEMENTATION_GUIDE.md](./IMPLEMENTATION_GUIDE.md)
- **Project Overview:** [PROJECT_SUMMARY.md](./PROJECT_SUMMARY.md)

---

## 🎉 Success!

You now have a powerful, automated CRM with:
- ✅ Intelligent lead scoring
- ✅ Automated deal management
- ✅ Flexible role-based permissions
- ✅ Modern, responsive UI
- ✅ RESTful API architecture
- ✅ Multi-tenant support

**Happy automating!** 🚀
