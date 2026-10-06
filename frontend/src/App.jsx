import React, { useEffect, useMemo, useState } from "react";
import {
  BarChart3,
  Bell,
  Building2,
  ChevronRight,
  ClipboardList,
  FileCheck2,
  LayoutDashboard,
  LogOut,
  Menu,
  Search,
  ShieldCheck,
  Users,
  WalletCards,
  X,
} from "lucide-react";

import keycloak, { initKeycloak } from "./auth";
window.keycloak = keycloak;
import {
  fetchAudit,
  fetchCertificates,
  fetchCitizens,
  fetchDashboard,
  fetchGrievances,
  fetchWelfare,
} from "./api";

import "./styles.css";

const MODULES = [
  {
    key: "citizens",
    label: "Citizen Management",
    icon: Users,
    description: "Manage citizens and grievances",
  },
  {
    key: "certificates",
    label: "Certificate Management",
    icon: FileCheck2,
    description: "Track certificate applications",
  },
  {
    key: "welfare",
    label: "Welfare & Budget",
    icon: WalletCards,
    description: "Manage welfare schemes and budgets",
  },
  {
    key: "dashboard",
    label: "Governance Analytics",
    icon: BarChart3,
    description: "Monitor governance performance",
  },
  {
    key: "audit",
    label: "Audit Logs",
    icon: ClipboardList,
    description: "Review system activity",
  },
];

function App() {
  const [auth, setAuth] = useState(null);
  const [ready, setReady] = useState(false);
  const [error, setError] = useState("");

  const [activeModule, setActiveModule] = useState("dashboard");
  const [sidebarOpen, setSidebarOpen] = useState(true);

  const [dashboard, setDashboard] = useState(null);
  const [citizens, setCitizens] = useState([]);
  const [grievances, setGrievances] = useState([]);
  const [certificates, setCertificates] = useState([]);
  const [welfare, setWelfare] = useState([]);
  const [audit, setAudit] = useState([]);

  const [loading, setLoading] = useState(false);
  const [dataError, setDataError] = useState("");

  /*
   * ---------------------------------------------------------
   * KEYCLOAK INITIALIZATION
   * ---------------------------------------------------------
   */

  useEffect(() => {
    let mounted = true;

    const initializeKeycloak = async () => {
      console.log("====================================");
      console.log("Starting Keycloak initialization...");
      console.log(
        "Keycloak URL:",
        import.meta.env.VITE_KEYCLOAK_URL || "http://localhost:8080",
      );
      console.log(
        "Realm:",
        import.meta.env.VITE_KEYCLOAK_REALM || "civicpulse",
      );
      console.log(
        "Client ID:",
        import.meta.env.VITE_KEYCLOAK_CLIENT_ID || "civicpulse-web",
      );
      console.log("Browser origin:", window.location.origin);
      console.log("====================================");

      try {
        const authenticated = await initKeycloak();

        if (!mounted) return;

        console.log("Keycloak initialization successful.");
        console.log("Authenticated:", authenticated);
        console.log("Username:", keycloak.tokenParsed?.preferred_username);
        console.log("Roles:", keycloak.tokenParsed?.realm_access?.roles);
        console.log("Token available:", Boolean(keycloak.token));

        setAuth(authenticated ? keycloak : null);
        setReady(true);
      } catch (err) {
        console.error("KEYCLOAK INITIALIZATION ERROR");
        console.error(err);
        console.error("Error message:", err?.message);

        if (!mounted) return;

        setError(err?.message || "Keycloak initialization failed.");

        setReady(true);
      }
    };

    initializeKeycloak();

    return () => {
      mounted = false;
    };
  }, []);
  /*
   * ---------------------------------------------------------
   * LOAD APPLICATION DATA
   * ---------------------------------------------------------
   */

  useEffect(() => {
    if (!auth) {
      return;
    }

    loadDashboardData();
  }, [auth]);

  const loadDashboardData = async () => {
    try {
      setLoading(true);
      setDataError("");

      const roles = keycloak.tokenParsed?.realm_access?.roles || [];

      const isAdmin = roles.includes("ADMIN");
      const isCommissioner = roles.includes("COMMISSIONER");
      const isOfficer = roles.includes("OFFICER");
      const isCitizen = roles.includes("CITIZEN");

      console.log("Current roles:", roles);

      const requests = {};

      // ============================
      // DASHBOARD
      // ============================
      if (isAdmin || isCommissioner || isOfficer) {
        requests.dashboard = fetchDashboard(keycloak.token);
      }

      // ============================
      // AUDIT
      // ADMIN + COMMISSIONER ONLY
      // ============================
      if (isAdmin || isCommissioner) {
        requests.audit = fetchAudit(keycloak.token);
      }

      // ============================
      // CITIZENS
      // ADMIN + OFFICER
      // ============================
      if (isAdmin || isOfficer) {
        requests.citizens = fetchCitizens(keycloak.token);
      }

      // ============================
      // WELFARE
      // ============================
      if (isAdmin || isCommissioner || isOfficer || isCitizen) {
        requests.welfare = fetchWelfare(keycloak.token);
      }

      // ============================
      // GRIEVANCES
      // ============================
      if (isAdmin || isCommissioner || isOfficer || isCitizen) {
        requests.grievances = fetchGrievances(keycloak.token);
      }

      // ============================
      // CERTIFICATES
      // ============================
      if (isAdmin || isCommissioner || isOfficer || isCitizen) {
        requests.certificates = fetchCertificates(keycloak.token);
      }

      // ============================
      // EXECUTE REQUESTS
      // ============================
      const results = await Promise.all(
        Object.entries(requests).map(async ([key, promise]) => {
          try {
            return [key, await promise];
          } catch (error) {
            console.error(`${key} data loading error:`, error);
            return [key, null];
          }
        }),
      );

      const data = Object.fromEntries(results);

      console.log("Loaded dashboard data:", data);

      // ============================
      // UPDATE EXISTING STATE
      // ============================

      if (data.dashboard !== undefined && data.dashboard !== null) {
        setDashboard(data.dashboard);
      }

      if (data.audit !== undefined && data.audit !== null) {
        setAudit(normalizeArray(data.audit));
      }

      if (data.citizens !== undefined && data.citizens !== null) {
        setCitizens(normalizeArray(data.citizens));
      }

      if (data.welfare !== undefined && data.welfare !== null) {
        setWelfare(normalizeArray(data.welfare));
      }

      if (data.grievances !== undefined && data.grievances !== null) {
        setGrievances(normalizeArray(data.grievances));
      }

      if (data.certificates !== undefined && data.certificates !== null) {
        setCertificates(normalizeArray(data.certificates));
      }
    } catch (error) {
      console.error("Dashboard data loading error:", error);
      setDataError(error?.message || "Unable to load governance data.");
    } finally {
      setLoading(false);
    }
  };

  /*
   * ---------------------------------------------------------
   * NORMALIZE API RESPONSES
   * ---------------------------------------------------------
   */

  const normalizeArray = (data) => {
    if (Array.isArray(data)) {
      return data;
    }

    if (Array.isArray(data?.content)) {
      return data.content;
    }

    if (Array.isArray(data?.data)) {
      return data.data;
    }

    if (Array.isArray(data?.items)) {
      return data.items;
    }

    return [];
  };

  /*
   * ---------------------------------------------------------
   * USER INFORMATION
   * ---------------------------------------------------------
   */

  const username = useMemo(() => {
    return (
      auth?.tokenParsed?.preferred_username || auth?.tokenParsed?.name || "User"
    );
  }, [auth]);

  const userRoles = useMemo(() => {
    return auth?.tokenParsed?.realm_access?.roles || [];
  }, [auth]);

  const applicationRole = useMemo(() => {
    const allowedRoles = ["ADMIN", "COMMISSIONER", "OFFICER", "CITIZEN"];

    return allowedRoles.find((role) => userRoles.includes(role)) || "USER";
  }, [userRoles]);

  /*
   * ---------------------------------------------------------
   * LOGOUT
   * ---------------------------------------------------------
   */

  const handleLogout = () => {
    keycloak.logout({
      redirectUri: window.location.origin,
    });
  };

  /*
   * ---------------------------------------------------------
   * TOKEN REFRESH
   * ---------------------------------------------------------
   */

  useEffect(() => {
    if (!auth) {
      return;
    }

    const refreshTimer = setInterval(async () => {
      try {
        const refreshed = await keycloak.updateToken(60);

        if (refreshed) {
          console.log("Keycloak token refreshed.");
        }
      } catch (err) {
        console.error("Token refresh failed:", err);
      }
    }, 30000);

    return () => {
      clearInterval(refreshTimer);
    };
  }, [auth]);

  /*
   * ---------------------------------------------------------
   * LOADING SCREEN
   * ---------------------------------------------------------
   */

  if (!ready) {
    return (
      <div className="auth-state">
        <div className="auth-card">
          <div className="auth-spinner" />

          <h1>Connecting to CivicPulse Nexus</h1>

          <p>Connecting to the secure authentication service...</p>
        </div>
      </div>
    );
  }

  /*
   * ---------------------------------------------------------
   * KEYCLOAK ERROR SCREEN
   * ---------------------------------------------------------
   */

  if (error) {
    return (
      <div className="auth-state">
        <div className="auth-card auth-error">
          <ShieldCheck size={48} />

          <h1>Sign-in service unavailable</h1>

          <p>Unable to initialize Keycloak authentication.</p>

          <div className="error-details">
            <strong>Actual error:</strong>

            <pre>{error}</pre>
          </div>

          <div className="auth-actions">
            <button type="button" onClick={() => window.location.reload()}>
              Reload
            </button>
          </div>
        </div>
      </div>
    );
  }

  /*
   * ---------------------------------------------------------
   * NOT AUTHENTICATED
   * ---------------------------------------------------------
   */

  if (!auth) {
    return (
      <div className="auth-state">
        <div className="auth-card">
          <ShieldCheck size={48} />

          <h1>Authentication required</h1>

          <p>You are not authenticated with CivicPulse Nexus.</p>

          <button
            type="button"
            onClick={() =>
              keycloak.login({
                redirectUri: window.location.origin,
              })
            }
          >
            Sign in
          </button>
        </div>
      </div>
    );
  }

  /*
   * ---------------------------------------------------------
   * RENDER ACTIVE MODULE
   * ---------------------------------------------------------
   */

  const renderModule = () => {
    switch (activeModule) {
      case "citizens":
        return <CitizenModule citizens={citizens} grievances={grievances} />;

      case "certificates":
        return <CertificateModule certificates={certificates} />;

      case "welfare":
        return <WelfareModule welfare={welfare} />;

      case "audit":
        return <AuditModule audit={audit} />;

      case "dashboard":
      default:
        return (
          <DashboardModule
            dashboard={dashboard}
            citizens={citizens}
            grievances={grievances}
            certificates={certificates}
            welfare={welfare}
          />
        );
    }
  };

  /*
   * ---------------------------------------------------------
   * MAIN APPLICATION
   * ---------------------------------------------------------
   */

  return (
    <div className="app-shell">
      <aside className={`sidebar ${sidebarOpen ? "open" : "closed"}`}>
        <div className="sidebar-header">
          <div className="brand-mark">
            <Building2 size={22} />
          </div>

          {sidebarOpen && (
            <div>
              <div className="brand-title">CivicPulse</div>

              <div className="brand-subtitle">Nexus</div>
            </div>
          )}
        </div>

        <nav className="sidebar-nav">
          <button
            type="button"
            className={`nav-item ${
              activeModule === "dashboard" ? "active" : ""
            }`}
            onClick={() => setActiveModule("dashboard")}
          >
            <LayoutDashboard size={19} />

            {sidebarOpen && <span>Dashboard</span>}
          </button>

          {MODULES.filter((module) => module.key !== "dashboard").map(
            (module) => {
              const Icon = module.icon;

              return (
                <button
                  key={module.key}
                  type="button"
                  className={`nav-item ${
                    activeModule === module.key ? "active" : ""
                  }`}
                  onClick={() => setActiveModule(module.key)}
                >
                  <Icon size={19} />

                  {sidebarOpen && <span>{module.label}</span>}
                </button>
              );
            },
          )}
        </nav>

        <div className="sidebar-bottom">
          <button
            type="button"
            className="nav-item logout-item"
            onClick={handleLogout}
          >
            <LogOut size={19} />

            {sidebarOpen && <span>Sign out</span>}
          </button>
        </div>
      </aside>

      <main className="main-content">
        <header className="topbar">
          <button
            type="button"
            className="icon-button"
            onClick={() => setSidebarOpen(!sidebarOpen)}
          >
            {sidebarOpen ? <X size={20} /> : <Menu size={20} />}
          </button>

          <div className="topbar-title">
            <span>
              {MODULES.find((module) => module.key === activeModule)?.label ||
                "Governance Dashboard"}
            </span>
          </div>

          <div className="topbar-actions">
            <button type="button" className="icon-button" title="Notifications">
              <Bell size={20} />
            </button>

            <div className="user-menu">
              <div className="avatar">{username.charAt(0).toUpperCase()}</div>

              <div className="user-info">
                <strong>{username}</strong>

                <span>{applicationRole}</span>
              </div>
            </div>
          </div>
        </header>

        <section className="page-content">
          <div className="page-heading">
            <div>
              <h1>
                {MODULES.find((module) => module.key === activeModule)?.label ||
                  "Governance Dashboard"}
              </h1>

              <p>
                {MODULES.find((module) => module.key === activeModule)
                  ?.description || "Integrated smart governance platform"}
              </p>
            </div>

            <button
              type="button"
              className="refresh-button"
              onClick={loadDashboardData}
              disabled={loading}
            >
              {loading ? "Refreshing..." : "Refresh data"}
            </button>
          </div>

          {dataError && <div className="data-error">{dataError}</div>}

          {renderModule()}
        </section>
      </main>
    </div>
  );
}

/*
 * =========================================================
 * DASHBOARD
 * =========================================================
 */

function DashboardModule({
  dashboard,
  citizens,
  grievances,
  certificates,
  welfare,
}) {
  const metrics = [
    {
      label: "Citizens",
      value: dashboard?.citizens ?? citizens.length ?? 0,
      icon: Users,
    },
    {
      label: "Grievances",
      value: dashboard?.grievances ?? grievances.length ?? 0,
      icon: ClipboardList,
    },
    {
      label: "Certificates",
      value: dashboard?.certificates ?? certificates.length ?? 0,
      icon: FileCheck2,
    },
    {
      label: "Welfare Applications",
      value: dashboard?.welfareApplications ?? welfare.length ?? 0,
      icon: WalletCards,
    },
  ];

  return (
    <div className="module-content">
      <div className="metrics-grid">
        {metrics.map((metric) => {
          const Icon = metric.icon;

          return (
            <div className="metric-card" key={metric.label}>
              <div className="metric-icon">
                <Icon size={21} />
              </div>

              <div>
                <div className="metric-value">{formatNumber(metric.value)}</div>

                <div className="metric-label">{metric.label}</div>
              </div>
            </div>
          );
        })}
      </div>

      <div className="content-grid">
        <div className="panel">
          <div className="panel-header">
            <div>
              <h2>Governance Overview</h2>

              <p>Current platform activity</p>
            </div>

            <BarChart3 size={21} />
          </div>

          <div className="overview-list">
            <OverviewRow label="Citizens registered" value={citizens.length} />

            <OverviewRow
              label="Active grievances"
              value={
                grievances.filter(
                  (item) =>
                    String(item.status || "").toUpperCase() !== "RESOLVED",
                ).length
              }
            />

            <OverviewRow
              label="Certificate applications"
              value={certificates.length}
            />

            <OverviewRow label="Welfare records" value={welfare.length} />
          </div>
        </div>

        <div className="panel">
          <div className="panel-header">
            <div>
              <h2>Service Performance</h2>

              <p>Key operational indicators</p>
            </div>

            <ShieldCheck size={21} />
          </div>

          <div className="performance-list">
            <PerformanceRow
              label="Grievance resolution"
              value={dashboard?.grievanceResolutionRate ?? "—"}
            />

            <PerformanceRow
              label="Certificate SLA"
              value={dashboard?.certificateSla ?? "—"}
            />

            <PerformanceRow
              label="Welfare utilization"
              value={dashboard?.welfareUtilization ?? "—"}
            />

            <PerformanceRow
              label="Citizen satisfaction"
              value={dashboard?.citizenSatisfaction ?? "—"}
            />
          </div>
        </div>
      </div>
    </div>
  );
}

/*
 * =========================================================
 * CITIZEN MODULE
 * =========================================================
 */

function CitizenModule({ citizens, grievances }) {
  return (
    <div className="module-content">
      <div className="section-summary">
        <div>
          <strong>{citizens.length}</strong>
          <span>Citizens</span>
        </div>

        <div>
          <strong>{grievances.length}</strong>
          <span>Grievances</span>
        </div>
      </div>

      <div className="panel">
        <div className="panel-header">
          <div>
            <h2>Citizen Directory</h2>
            <p>Registered citizens in the governance system</p>
          </div>

          <Search size={20} />
        </div>

        <DataTable
          data={citizens}
          columns={["id", "name", "email", "phone", "status"]}
        />
      </div>

      <div className="panel">
        <div className="panel-header">
          <div>
            <h2>Recent Grievances</h2>
            <p>Citizen complaints and service requests</p>
          </div>

          <ClipboardList size={20} />
        </div>

        <DataTable
          data={grievances}
          columns={["id", "title", "status", "priority", "createdAt"]}
        />
      </div>
    </div>
  );
}

/*
 * =========================================================
 * CERTIFICATE MODULE
 * =========================================================
 */

function CertificateModule({ certificates }) {
  return (
    <div className="module-content">
      <div className="panel">
        <div className="panel-header">
          <div>
            <h2>Certificate Applications</h2>

            <p>Track citizen certificate requests and processing</p>
          </div>

          <FileCheck2 size={20} />
        </div>

        <DataTable
          data={certificates}
          columns={["id", "citizenName", "serviceType", "status", "createdAt"]}
        />
      </div>
    </div>
  );
}

/*
 * =========================================================
 * WELFARE MODULE
 * =========================================================
 */

function WelfareModule({ welfare }) {
  return (
    <div className="module-content">
      <div className="panel">
        <div className="panel-header">
          <div>
            <h2>Welfare Schemes & Budget</h2>

            <p>Monitor applications, beneficiaries and scheme activity</p>
          </div>

          <WalletCards size={20} />
        </div>

        <DataTable
          data={welfare}
          columns={["id", "schemeName", "beneficiaryName", "status", "amount"]}
        />
      </div>
    </div>
  );
}

/*
 * =========================================================
 * AUDIT MODULE
 * =========================================================
 */

function AuditModule({ audit }) {
  return (
    <div className="module-content">
      <div className="panel">
        <div className="panel-header">
          <div>
            <h2>Audit Logs</h2>

            <p>Security and governance activity history</p>
          </div>

          <ShieldCheck size={20} />
        </div>

        <DataTable
          data={audit}
          columns={[
            "id",
            "actor",
            "role",
            "action",
            "entityType",
            "entityId",
            "createdAt",
          ]}
        />
      </div>
    </div>
  );
}

/*
 * =========================================================
 * REUSABLE COMPONENTS
 * =========================================================
 */

function OverviewRow({ label, value }) {
  return (
    <div className="overview-row">
      <span>{label}</span>

      <strong>{formatNumber(value)}</strong>
    </div>
  );
}

function PerformanceRow({ label, value }) {
  return (
    <div className="performance-row">
      <span>{label}</span>

      <strong>{formatValue(value)}</strong>
    </div>
  );
}

function DataTable({ data, columns }) {
  if (!data || data.length === 0) {
    return <div className="empty-state">No records available.</div>;
  }

  return (
    <div className="table-wrapper">
      <table>
        <thead>
          <tr>
            {columns.map((column) => (
              <th key={column}>{formatColumnName(column)}</th>
            ))}
          </tr>
        </thead>

        <tbody>
          {data.map((item, index) => (
            <tr key={item.id ?? item._id ?? index}>
              {columns.map((column) => (
                <td key={column}>{formatValue(item[column])}</td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

/*
 * =========================================================
 * HELPERS
 * =========================================================
 */

function formatNumber(value) {
  if (value === null || value === undefined || value === "") {
    return "0";
  }

  if (typeof value === "number") {
    return value.toLocaleString();
  }

  return value;
}

function formatValue(value) {
  if (value === null || value === undefined || value === "") {
    return "—";
  }

  if (typeof value === "object") {
    return JSON.stringify(value);
  }

  return String(value);
}

function formatColumnName(column) {
  return column
    .replace(/([A-Z])/g, " $1")
    .replace(/^./, (letter) => letter.toUpperCase());
}

export default App;
