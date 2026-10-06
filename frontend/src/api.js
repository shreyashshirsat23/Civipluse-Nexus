const API_URL =
  import.meta.env.VITE_API_URL || "http://localhost:8081/api";

/* =====================================================
   GENERIC API FUNCTION
   ===================================================== */

export async function api(path, options = {}, token = "") {
  const headers = {
    "Content-Type": "application/json",
    ...(options.headers || {}),
  };

  // Accept either:
  // 1. JWT string
  // 2. Keycloak object
  const accessToken =
    typeof token === "string"
      ? token
      : token?.token;

  if (accessToken) {
    headers.Authorization = `Bearer ${accessToken}`;
  }

  const response = await fetch(`${API_URL}${path}`, {
    ...options,
    headers,
  });

  if (!response.ok) {
    const body = await response.json().catch(() => ({}));

    throw new Error(
      body.message ||
        body.error ||
        `Request failed (${response.status})`
    );
  }

  return response.status === 204
    ? null
    : response.json();
}


/* =====================================================
   DASHBOARD
   Backend:
   GET /api/dashboard/summary
   ===================================================== */

export async function fetchDashboard(token = "") {
  return api("/dashboard/summary", {}, token);
}


/* =====================================================
   CITIZENS
   Backend:
   GET /api/citizens
   ===================================================== */

export async function fetchCitizens(token = "") {
  return api("/citizens", {}, token);
}


/* =====================================================
   GRIEVANCES
   Backend:
   GET /api/grievances
   ===================================================== */

export async function fetchGrievances(token = "") {
  return api("/grievances", {}, token);
}


/* =====================================================
   CERTIFICATES
   Backend:
   GET /api/certificates
   ===================================================== */

export async function fetchCertificates(token = "") {
  return api("/certificates", {}, token);
}


/* =====================================================
   WELFARE
   Backend:
   GET /api/welfare/schemes
   GET /api/welfare/applications
   ===================================================== */

export async function fetchWelfare(token = "") {
  const [schemes, applications] = await Promise.all([
    api("/welfare/schemes", {}, token),
    api("/welfare/applications", {}, token),
  ]);

  return {
    schemes,
    applications,
  };
}


/* =====================================================
   AUDIT
   Backend:
   GET /api/audit
   ===================================================== */

export async function fetchAudit(token = "") {
  return api("/audit", {}, token);
}


/* =====================================================
   APPLICATIONS
   Backend:
   GET /api/applications
   ===================================================== */

export async function fetchApplications(token = "") {
  return api("/applications", {}, token);
}


/* =====================================================
   BUDGETS
   Backend:
   GET /api/budgets
   ===================================================== */

export async function fetchBudgets(token = "") {
  return api("/budgets", {}, token);
}


/* =====================================================
   DEPARTMENTS
   Backend:
   GET /api/departments
   ===================================================== */

export async function fetchDepartments(token = "") {
  return api("/departments", {}, token);
}


/* =====================================================
   WELFARE OPERATIONS
   ===================================================== */

/*
 * POST /api/welfare/schemes
 */
export async function createWelfareScheme(
  data,
  token = ""
) {
  return api(
    "/welfare/schemes",
    {
      method: "POST",
      body: JSON.stringify(data),
    },
    token
  );
}


/*
 * POST /api/welfare/applications
 */
export async function applyForWelfare(
  data,
  token = ""
) {
  return api(
    "/welfare/applications",
    {
      method: "POST",
      body: JSON.stringify(data),
    },
    token
  );
}


/*
 * PATCH /api/welfare/applications/{id}/approve?amount=...
 */
export async function approveWelfareApplication(
  id,
  amount,
  token = ""
) {
  return api(
    `/welfare/applications/${id}/approve?amount=${encodeURIComponent(amount)}`,
    {
      method: "PATCH",
    },
    token
  );
}


/* =====================================================
   GRIEVANCE OPERATIONS
   ===================================================== */

/*
 * POST /api/grievances
 */
export async function createGrievance(
  data,
  token = ""
) {
  return api(
    "/grievances",
    {
      method: "POST",
      body: JSON.stringify(data),
    },
    token
  );
}


/*
 * PATCH /api/grievances/{id}/status
 */
export async function updateGrievanceStatus(
  id,
  status,
  officer = "Officer",
  token = ""
) {
  return api(
    `/grievances/${id}/status?status=${encodeURIComponent(
      status
    )}&officer=${encodeURIComponent(officer)}`,
    {
      method: "PATCH",
    },
    token
  );
}


/* =====================================================
   CITIZEN OPERATIONS
   ===================================================== */

/*
 * POST /api/citizens
 */
export async function createCitizen(
  data,
  token = ""
) {
  return api(
    "/citizens",
    {
      method: "POST",
      body: JSON.stringify(data),
    },
    token
  );
}


/* =====================================================
   APPLICATION OPERATIONS
   ===================================================== */

/*
 * POST /api/applications
 */
export async function createApplication(
  data,
  token = ""
) {
  return api(
    "/applications",
    {
      method: "POST",
      body: JSON.stringify(data),
    },
    token
  );
}


/*
 * PATCH /api/applications/{id}/review
 */
export async function reviewApplication(
  id,
  stage,
  status,
  reviewer = "Reviewer",
  token = ""
) {
  return api(
    `/applications/${id}/review?stage=${encodeURIComponent(
      stage
    )}&status=${encodeURIComponent(
      status
    )}&reviewer=${encodeURIComponent(
      reviewer
    )}`,
    {
      method: "PATCH",
    },
    token
  );
}


/*
 * PATCH /api/applications/{id}/approve
 */
export async function approveApplication(
  id,
  reviewer = "COMMISSIONER",
  token = ""
) {
  return api(
    `/applications/${id}/approve?reviewer=${encodeURIComponent(
      reviewer
    )}`,
    {
      method: "PATCH",
    },
    token
  );
}


/* =====================================================
   BUDGET OPERATIONS
   ===================================================== */

/*
 * POST /api/budgets
 */
export async function createBudget(
  data,
  token = ""
) {
  return api(
    "/budgets",
    {
      method: "POST",
      body: JSON.stringify(data),
    },
    token
  );
}


/* =====================================================
   DEPARTMENT OPERATIONS
   ===================================================== */

/*
 * POST /api/departments
 */
export async function createDepartment(
  data,
  token = ""
) {
  return api(
    "/departments",
    {
      method: "POST",
      body: JSON.stringify(data),
    },
    token
  );
}