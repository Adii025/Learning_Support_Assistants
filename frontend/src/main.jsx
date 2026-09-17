import React, { useEffect, useState } from "react";
import { createRoot } from "react-dom/client";
import {
  BrowserRouter,
  useNavigate,
  useLocation,
  Routes,
  Route,
  Link,
} from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap-icons/font/bootstrap-icons.css";
import axios from "axios";
import "./style.css";

const API = "http://localhost:8081/api";

const api = axios.create({
  baseURL: API,
});

// Every protected endpoint now requires "Authorization: Bearer <token>".
// This attaches whatever token login stored, on every request.
api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// If the token is missing/expired, the backend returns 401 -> bounce to login.
api.interceptors.response.use(
  (res) => res,
  (err) => {
    if (err.response?.status === 401) {
      localStorage.clear();
      window.location.href = "/login";
    }
    return Promise.reject(err);
  }
);

// ===============================
// LAYOUT
// ===============================

function Layout({ children }) {
  const nav = useNavigate();
  const loc = useLocation();

  const u = JSON.parse(localStorage.getItem("user") || "null");

  const role = u?.role || "PARENT";

  const menus =
    role === "ADMIN"
      ? [
          "Dashboard",
          "Users",
          "Students",
          "LSAs",
          "Matching",
          "Sessions",
          "Audit Logs",
        ]
      : role === "LSA"
      ? [
          "Dashboard",
          "Profile",
          "Availability",
          "Match Requests",
          "Assigned Students",
          "Session Notes",
        ]
      : [
          "Dashboard",
          "My Children",
          "Request LSA",
          "Sessions",
        ];

  const path = (s) =>
    "/" + s.toLowerCase().replaceAll(" ", "-");

  const logout = () => {
    localStorage.clear();
    nav("/login");
  };

  return (
    <div className="app">
      <aside>
        <div className="brand">
          Habot<span>Connect</span>
        </div>

        <small>{role} PORTAL</small>

        {menus.map((m) => (
          <Link
            className={
              loc.pathname === path(m) ? "active" : ""
            }
            to={path(m)}
            key={m}
          >
            <i className="bi bi-grid"></i>
            {m}
          </Link>
        ))}

        <button className="logout" onClick={logout}>
          Logout
        </button>
      </aside>

      <main>
        <header>
          <div>
            <b>
              {menus.find(
                (m) => loc.pathname === path(m)
              ) || "Dashboard"}
            </b>

            <div className="muted">
              Student–LSA Matching Platform
            </div>
          </div>

          <div className="user">
            {u?.name || "Demo User"}{" "}
            <span>{role}</span>
          </div>
        </header>

        {children}
      </main>
    </div>
  );
}

// ===============================
// LOGIN
// ===============================

function Login() {
  const [form, setForm] = useState({
    email: "",
    password: "",
  });

  const [err, setErr] = useState("");

  const nav = useNavigate();

  async function go(e) {
    e.preventDefault();

    try {
      const r = await api.post("/auth/login", form);

      localStorage.setItem("token", r.data.token);
      localStorage.setItem(
        "user",
        JSON.stringify(r.data)
      );

      nav("/dashboard");
    } catch (x) {
      setErr(
        x.response?.data?.error ||
          "Login failed"
      );
    }
  }

  return (
    <div className="auth">
      <div className="authbox">
        <div className="brand big">
          Habot<span>Connect</span>
        </div>

        <p className="muted">
          Student–LSA Matching Platform
        </p>

        <h3>Welcome back</h3>

        {err && (
          <div className="alert alert-danger">
            {err}
          </div>
        )}

        <form onSubmit={go}>
          <input
            required
            placeholder="Email"
            value={form.email}
            onChange={(e) =>
              setForm({
                ...form,
                email: e.target.value,
              })
            }
          />

          <input
            required
            type="password"
            placeholder="Password"
            value={form.password}
            onChange={(e) =>
              setForm({
                ...form,
                password: e.target.value,
              })
            }
          />

          <button type="submit">
            Sign In
          </button>
        </form>

        <p className="hint">
          Demo accounts must be registered first.
        </p>

        <Link to="/register">
          Create an account
        </Link>
      </div>
    </div>
  );
}

// ===============================
// REGISTER
// ===============================

function Register() {
  const [form, setForm] = useState({
    name: "",
    email: "",
    password: "",
    role: "PARENT",
  });

  const [err, setErr] = useState("");

  const nav = useNavigate();

  async function go(e) {
    e.preventDefault();

    try {
      await api.post(
        "/auth/register",
        form
      );

      nav("/login");
    } catch (x) {
      setErr(
        x.response?.data?.error ||
          "Registration failed"
      );
    }
  }

  return (
    <div className="auth">
      <div className="authbox">
        <div className="brand big">
          Habot<span>Connect</span>
        </div>

        <h3>Create account</h3>

        {err && (
          <div className="alert alert-danger">
            {err}
          </div>
        )}

        <form onSubmit={go}>
          <input
            required
            placeholder="Full name"
            value={form.name}
            onChange={(e) =>
              setForm({
                ...form,
                name: e.target.value,
              })
            }
          />

          <input
            required
            type="email"
            placeholder="Email"
            value={form.email}
            onChange={(e) =>
              setForm({
                ...form,
                email: e.target.value,
              })
            }
          />

          <input
            required
            type="password"
            placeholder="Password"
            value={form.password}
            onChange={(e) =>
              setForm({
                ...form,
                password: e.target.value,
              })
            }
          />

          <select
            value={form.role}
            onChange={(e) =>
              setForm({
                ...form,
                role: e.target.value,
              })
            }
          >
            <option value="PARENT">
              PARENT
            </option>

            <option value="LSA">
              LSA
            </option>

            <option value="ADMIN">
              ADMIN
            </option>
          </select>

          <button type="submit">
            Register
          </button>
        </form>

        <Link to="/login">
          Back to login
        </Link>
      </div>
    </div>
  );
}

// ===============================
// PAGE TITLES
// ===============================

const titles = {
  dashboard: "Dashboard",
  "my-children": "My Children",
  "request-lsa": "Request LSA",
  sessions: "Sessions",

  profile: "My Profile",
  availability: "Availability",
  "match-requests": "Match Requests",
  "assigned-students": "Assigned Students",
  "session-notes": "Session Notes",

  users: "Users",
  students: "Students",
  lsas: "LSAs",
  matching: "Matching",
  "audit-logs": "Audit Logs",
};

// ===============================
// DASHBOARD
// ===============================

function Dashboard() {
  const [u, setU] = useState({});

  useEffect(() => {
    api
      .get("/dashboard")
      .then((r) => setU(r.data))
      .catch((err) =>
        console.log(
          "Dashboard error:",
          err
        )
      );
  }, []);

  const currentUser = JSON.parse(
    localStorage.getItem("user") || "{}"
  );

  return (
    <>
      <div className="hero">
        <div>
          <h1>
            Good day,{" "}
            {currentUser.name || "User"} 👋
          </h1>

          <p>
            Here is your platform overview.
          </p>
        </div>
      </div>

      <div className="cards">
        {[
          ["users", "Users"],
          ["parents", "Parents"],
          ["students", "Students"],
          ["lsa", "Active LSAs"],
          ["requests", "Requests"],
          ["matches", "Matches"],
          ["sessions", "Sessions"],
        ].map(([key, title]) => (
          <div
            className="cardx"
            key={key}
          >
            <div className="icon">
              <i className="bi bi-people"></i>
            </div>

            <div>
              <small>{title}</small>

              <h2>
                {u[key] ?? 0}
              </h2>
            </div>
          </div>
        ))}
      </div>
    </>
  );
}

// ===============================
// DATA PAGE
// ===============================

function DataPage({
  type,
  title,
  fields,
  hidden = {},
  selects = [], // optional: [{ name, label, fetchType, filter(item,user), labelKey, valueKey }]
  readOnly = false, // when true, hide the add-form entirely (view-only table)
  filterRows, // optional: (row, user) => boolean, to only show rows relevant to the viewer
  ownRecordMatch, // optional: (row, user) => boolean. If a row matches, the
  // form becomes an EDIT of that row (pre-filled, PUT on submit) instead of
  // always trying to INSERT a new one. Needed for 1-to-1 records (e.g. an
  // LSA's own profile) where a second POST would violate a unique
  // constraint because the row already exists.
  statusActions, // optional: array of status strings (e.g. ["ACCEPTED","REJECTED"])
  // rendered as one button per row, which PUTs the row back with status
  // changed to that value. For read-only pages where the viewer can react
  // to a row (e.g. an LSA accepting/rejecting a parent's match request)
  // without being able to create brand new rows themselves.
}) {
  // `hidden` maps a field name (e.g. "parentId") to a fixed value that must
  // be sent with every POST but should NOT be shown as an input box, because
  // the backend requires these foreign keys (parentId/userId/studentId) and
  // the person filling the form has no business typing raw database ids.
  const currentUser = JSON.parse(
    localStorage.getItem("user") || "{}"
  );

  const [data, setData] = useState([]);

  const [form, setForm] = useState({});

  const [error, setError] = useState("");

  // when set, we're editing this existing row's id (PUT) instead of
  // creating a new one (POST) — see ownRecordMatch above.
  const [editingId, setEditingId] = useState(null);

  // one options list per configured select, keyed by field name
  const [selectOptionsByField, setSelectOptionsByField] = useState({});

  useEffect(() => {
    selects.forEach((sel) => {
      api
        .get("/" + sel.fetchType)
        .then((r) => {
          const rows = Array.isArray(r.data) ? r.data : [];
          setSelectOptionsByField((prev) => ({
            ...prev,
            [sel.name]: sel.filter
              ? rows.filter((row) => sel.filter(row, currentUser))
              : rows,
          }));
        })
        .catch(() =>
          setSelectOptionsByField((prev) => ({
            ...prev,
            [sel.name]: [],
          }))
        );
    });
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const load = () => {
    api
      .get("/" + type)
      .then((r) => {
        const rows = Array.isArray(r.data) ? r.data : [];
        setData(
          filterRows ? rows.filter((row) => filterRows(row, currentUser)) : rows
        );

        if (ownRecordMatch) {
          const mine = rows.find((row) => ownRecordMatch(row, currentUser));
          if (mine) {
            setEditingId(mine.id);
            const prefill = {};
            fields.forEach((f) => {
              if (mine[f] !== undefined && mine[f] !== null) {
                prefill[f] = mine[f];
              }
            });
            setForm(prefill);
          }
        }
      })
      .catch((err) => {
        console.log(
          "GET error:",
          err
        );

        setError(
          err.response?.data?.error ||
            "Unable to load data"
        );
      });
  };

  useEffect(() => {
    load();
  }, []);

  async function add(e) {
    e.preventDefault();

    setError("");

    // Merge in the hidden foreign keys (e.g. parentId from the logged-in
    // parent's own profile) that the backend requires but that isn't
    // something the user should be typing into a text box.
    const resolvedHidden = {};
    Object.keys(hidden).forEach((key) => {
      resolvedHidden[key] = hidden[key](currentUser);
    });

    const missing = Object.entries(resolvedHidden).find(
      ([, v]) => v === null || v === undefined
    );
    if (missing) {
      setError(
        "Your account has no linked profile id yet (profileId is missing). Log out and log back in; if that doesn't help, this account was created before the fix and needs a one-time database backfill."
      );
      return;
    }

    try {
      if (editingId) {
        await api.put(
          "/" + type + "/" + editingId,
          { ...form, ...resolvedHidden }
        );
      } else {
        await api.post(
          "/" + type,
          { ...form, ...resolvedHidden }
        );
      }

      if (!editingId) {
        setForm({});
      }

      load();
    } catch (err) {
      console.log(
        editingId ? "PUT error:" : "POST error:",
        err
      );

      setError(
        err.response?.data?.error ||
          (editingId ? "Unable to update record" : "Unable to add record")
      );
    }
  }

  function updateField(
    field,
    value
  ) {
    setForm({
      ...form,
      [field]: value,
    });
  }

  async function applyStatusAction(row, status) {
    setError("");
    try {
      await api.put("/" + type + "/" + row.id, { ...row, status });
      load();
    } catch (err) {
      setError(
        err.response?.data?.error || "Unable to update record"
      );
    }
  }

  return (
    <>
      <div className="sectionhead">
        <div>
          <h2>{title}</h2>

          <p className="muted">
            Manage{" "}
            {title.toLowerCase()}.
          </p>
        </div>
      </div>

      {error && (
        <div className="alert alert-danger">
          {error}
        </div>
      )}

      <div className="panel">
        {readOnly ? (
          <p className="muted">
            This view is read-only — sessions are scheduled by your LSA/Admin.
          </p>
        ) : (
        <form
          className="inlineform"
          onSubmit={add}
        >
          {selects.map((sel) => (
            <select
              key={sel.name}
              required
              value={form[sel.name] || ""}
              onChange={(e) =>
                updateField(sel.name, e.target.value)
              }
            >
              <option value="" disabled>
                {sel.label}
              </option>
              {(selectOptionsByField[sel.name] || []).map((opt) => (
                <option
                  key={opt[sel.valueKey || "id"]}
                  value={opt[sel.valueKey || "id"]}
                >
                  {opt[sel.labelKey || "name"]}
                </option>
              ))}
            </select>
          ))}

          {fields.map((field) => (
            <input
              key={field}
              // scheduledAt maps to a java.time.LocalDateTime on the backend,
              // which needs an ISO string like "2026-09-17T12:00" — a free
              // text box lets people type "12:00:00" which fails to parse.
              // A native datetime-local input produces the right format.
              type={field === "scheduledAt" ? "datetime-local" : "text"}
              placeholder={field
                .replaceAll(
                  "_",
                  " "
                )}
              value={
                form[field] || ""
              }
              onChange={(e) =>
                updateField(
                  field,
                  e.target.value
                )
              }
            />
          ))}

          <button type="submit">
            {editingId ? "Save" : "Add"}
          </button>
        </form>
        )}
      </div>

      <div className="panel">
        <div className="table-responsive">
          <table>
            <thead>
              <tr>
                {fields.map(
                  (field) => (
                    <th key={field}>
                      {field}
                    </th>
                  )
                )}
                {statusActions && <th>actions</th>}
              </tr>
            </thead>

            <tbody>
              {data.map(
                (item, index) => (
                  <tr
                    key={
                      item.id ||
                      index
                    }
                  >
                    {fields.map(
                      (field) => (
                        <td
                          key={
                            field
                          }
                        >
                          {typeof item[
                            field
                          ] ===
                          "object"
                            ? item[
                                field
                              ]?.name ||
                              item[
                                field
                              ]?.id ||
                              "—"
                            : String(
                                item[
                                  field
                                ] ??
                                  "—"
                              )}
                        </td>
                      )
                    )}
                    {statusActions && (
                      <td>
                        {statusActions.map((s) => (
                          <button
                            key={s}
                            type="button"
                            className="linkbutton"
                            onClick={() => applyStatusAction(item, s)}
                          >
                            {s}
                          </button>
                        ))}
                      </td>
                    )}
                  </tr>
                )
              )}
            </tbody>
          </table>
        </div>

        {!data.length && (
          <div className="empty">
            No records yet.
          </div>
        )}
      </div>
    </>
  );
}

// ===============================
// PAGE ROUTER
// ===============================

// Parents should only ever see sessions that belong to their own children.
// This does its own two-step fetch (my students -> sessions for those
// students) rather than forcing that logic into the generic DataPage.
function ParentSessionsView() {
  const [rows, setRows] = useState([]);
  const [loading, setLoading] = useState(true);
  const currentUser = JSON.parse(
    localStorage.getItem("user") || "{}"
  );

  useEffect(() => {
    Promise.all([
      api.get("/students"),
      api.get("/sessions"),
    ])
      .then(([studentsRes, sessionsRes]) => {
        const myStudentIds = new Set(
          (studentsRes.data || [])
            .filter((s) => s.parentId === currentUser.profileId)
            .map((s) => s.id)
        );
        setRows(
          (sessionsRes.data || []).filter((s) =>
            myStudentIds.has(s.studentId)
          )
        );
      })
      .finally(() => setLoading(false));
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const fields = ["scheduledAt", "status", "notes"];

  return (
    <>
      <div className="sectionhead">
        <div>
          <h2>Sessions</h2>
          <p className="muted">Sessions scheduled for your children.</p>
        </div>
      </div>

      <div className="panel">
        <div className="table-responsive">
          <table>
            <thead>
              <tr>
                {fields.map((f) => (
                  <th key={f}>{f}</th>
                ))}
              </tr>
            </thead>
            <tbody>
              {rows.map((item, i) => (
                <tr key={item.id || i}>
                  {fields.map((f) => (
                    <td key={f}>{String(item[f] ?? "—")}</td>
                  ))}
                </tr>
              ))}
            </tbody>
          </table>
        </div>
        {!loading && !rows.length && (
          <div className="empty">
            No sessions scheduled yet — these are set up by your LSA/Admin
            once your child is matched.
          </div>
        )}
      </div>
    </>
  );
}

function Page({ name }) {
  const currentUser = JSON.parse(
    localStorage.getItem("user") || "{}"
  );
  const role = currentUser.role || "PARENT";

  if (name === "dashboard") {
    return <Dashboard />;
  }

  if (name === "my-children") {
    return (
      <DataPage
        type="students"
        title="My Children"
        fields={[
          "name",
          "age",
          "grade",
          "learningDifficulty",
          "location",
        ]}
        hidden={{
          // students.parent_id is required by the backend; it must be the
          // logged-in parent's OWN parents.id (profileId), not their user id.
          parentId: (u) => u.profileId,
        }}
        filterRows={(row, u) => row.parentId === u.profileId}
      />
    );
  }

  if (name === "request-lsa") {
    return (
      <DataPage
        type="match-requests"
        title="Request LSA"
        fields={[
          "preferredSpecialization",
          "status",
        ]}
        hidden={{
          parentId: (u) => u.profileId,
        }}
        filterRows={(row, u) => row.parentId === u.profileId}
        selects={[
          {
            name: "studentId",
            label: "Choose your child",
            fetchType: "students",
            // only show this parent's own children in the dropdown
            filter: (student, u) => student.parentId === u.profileId,
            labelKey: "name",
            valueKey: "id",
          },
        ]}
      />
    );
  }

  if (name === "sessions") {
    if (role === "PARENT") {
      // Parents can only ever VIEW sessions for their own children.
      // Scheduling a session (picking any student/LSA in the system) is an
      // Admin/LSA action, not something a parent should be able to do.
      return <ParentSessionsView />;
    }
    return (
      <DataPage
        type="sessions"
        title="Sessions"
        fields={[
          "scheduledAt",
          "status",
          "notes",
        ]}
        selects={[
          { name: "studentId", label: "Student", fetchType: "students", labelKey: "name", valueKey: "id" },
          { name: "lsaId", label: "LSA", fetchType: "lsa", labelKey: "userName", valueKey: "id" },
        ]}
      />
    );
  }

  if (name === "profile") {
    return (
      <DataPage
        type="lsa"
        title="My Profile"
        fields={[
          "specialization",
          "experienceYears",
          "location",
          "bio",
        ]}
        hidden={{
          // lsa_profiles.user_id is required by the backend.
          userId: (u) => u.userId,
        }}
        ownRecordMatch={(row, u) => row.userId === u.userId}
      />
    );
  }

  if (name === "availability") {
    return (
      <DataPage
        type="availability"
        title="Availability"
        fields={[
          "dayOfWeek",
          "startTime",
          "endTime",
          "available",
        ]}
        hidden={{
          // availability.lsa_id is required; it's this LSA's own profile id.
          lsaId: (u) => u.profileId,
        }}
      />
    );
  }

  if (name === "match-requests") {
    // This is the LSA's view of parents' requests. LSAs respond to
    // requests (Accept/Reject) — they don't create brand new ones, so this
    // is read-only with action buttons rather than an add-form (which was
    // failing before: it had no way to supply the required studentId/parentId).
    return (
      <DataPage
        type="match-requests"
        title="Match Requests"
        fields={[
          "preferredSpecialization",
          "status",
        ]}
        readOnly
        statusActions={["ACCEPTED", "REJECTED"]}
      />
    );
  }

  if (name === "assigned-students") {
    return (
      <DataPage
        type="matches"
        title="Assigned Students"
        fields={[
          "status",
          "score",
        ]}
        hidden={{
          // matches.lsa_id is required; it's this LSA's own profile id.
          lsaId: (u) => u.profileId,
        }}
        selects={[
          { name: "studentId", label: "Student", fetchType: "students", labelKey: "name", valueKey: "id" },
          {
            name: "requestId",
            // A Match should be created once a parent's request has been
            // ACCEPTED by an LSA -- not while it's still PENDING (a pending
            // request hasn't been agreed to by anyone yet).
            label: "Accepted request",
            fetchType: "match-requests",
            filter: (r) => r.status === "ACCEPTED",
            labelKey: "preferredSpecialization",
            valueKey: "id",
          },
        ]}
      />
    );
  }

  if (name === "session-notes") {
    return (
      <DataPage
        type="sessions"
        title="Session Notes"
        fields={[
          "scheduledAt",
          "status",
          "notes",
        ]}
        hidden={{
          // sessions.lsa_id is required; it's this LSA's own profile id.
          lsaId: (u) => u.profileId,
        }}
        selects={[
          { name: "studentId", label: "Student", fetchType: "students", labelKey: "name", valueKey: "id" },
        ]}
      />
    );
  }

  if (name === "users") {
    return (
      <DataPage
        type="users"
        title="Users"
        fields={[
          "name",
          "email",
          "role",
        ]}
      />
    );
  }

  if (name === "students") {
    return (
      <DataPage
        type="students"
        title="Students"
        fields={[
          "name",
          "age",
          "grade",
          "learningDifficulty",
          "location",
        ]}
      />
    );
  }

  if (name === "lsas") {
    return (
      <DataPage
        type="lsa"
        title="LSAs"
        fields={[
          "specialization",
          "experienceYears",
          "location",
          "active",
        ]}
      />
    );
  }

  if (name === "matching") {
    return (
      <DataPage
        type="matches"
        title="Matching"
        fields={[
          "status",
          "score",
        ]}
        selects={[
          { name: "studentId", label: "Student", fetchType: "students", labelKey: "name", valueKey: "id" },
          { name: "lsaId", label: "LSA", fetchType: "lsa", labelKey: "userName", valueKey: "id" },
          {
            name: "requestId",
            // A Match should be created once a parent's request has been
            // ACCEPTED by an LSA -- not while it's still PENDING (a pending
            // request hasn't been agreed to by anyone yet).
            label: "Accepted request",
            fetchType: "match-requests",
            filter: (r) => r.status === "ACCEPTED",
            labelKey: "preferredSpecialization",
            valueKey: "id",
          },
        ]}
      />
    );
  }

  return (
    <DataPage
      type="audit-logs"
      title="Audit Logs"
      fields={[
        "userId",
        "action",
        "entityName",
        "entityId",
        "createdAt",
      ]}
    />
  );
}

// ===============================
// APP
// ===============================

function App() {
  return (
    <Routes>
      <Route
        path="/login"
        element={<Login />}
      />

      <Route
        path="/register"
        element={<Register />}
      />

      <Route
        path="*"
        element={
          <Layout>
            <Routes>
              {Object.keys(titles).map(
                (key) => (
                  <Route
                    key={key}
                    path={"/" + key}
                    element={
                      <Page
                        key={key}
                        name={key}
                      />
                    }
                  />
                )
              )}
            </Routes>
          </Layout>
        }
      />
    </Routes>
  );
}

// ===============================
// APPLICATION START
// ===============================

createRoot(
  document.getElementById("root")
).render(
  <BrowserRouter>
    <App />
  </BrowserRouter>
);
