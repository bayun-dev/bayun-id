import{B as R,R as C,j as e,u as I,r as o,c as S,E as A}from"./3f2d47e7.js";/* empty css        */import{I as p,H as L,L as k,E as c}from"./0e2defe6.js";import{C as T,q as U}from"./98679543.js";import{q as b}from"./4a98f407.js";class q extends R{constructor(t){super(C.POST,"/api/methods/auth.signIn",t,{headers:{"Content-Type":"application/x-www-form-urlencoded",Accept:"application/json"}})}}const F=()=>{const d=I(),[t,g]=o.useState(""),[j,n]=o.useState(void 0),w=s=>{const r=s.currentTarget.value.trim();g(r),n(void 0)},[u,E]=o.useState(""),[v,l]=o.useState(void 0),[x,N]=o.useState(!1),P=s=>{const r=s.currentTarget.value;E(r),l(void 0)};async function y(){const s=b(t),r=U(u);if(s||r){n(s),l(r);return}new q({username:t,password:u}).execute().then(a=>{if(a.data.ok)window.location.replace(a.data.redirectUri);else throw c.INTERNAL_ERROR}).catch(a=>{var h;if(!(a.response&&a.response.data)){d.show.internalError();return}const i=a.response.data;i.type===c.BAD_REQUEST_PARAMETERS?(h=i.parameters)==null||h.forEach(m=>{m==="username"?n("Invalid"):m==="password"&&l("Invalid")}):i.type===c.USERNAME_UNOCCUPIED?n("Account doesn't exist"):i.type===c.PASSWORD_INVALID?l("Incorrect"):d.show.internalError()})}return e.jsx(e.Fragment,{children:e.jsxs("div",{className:"w-full xs:w-xs p-4 space-y-8",children:[e.jsx("div",{className:"text-center",children:e.jsx("span",{className:"text-3xl font-medium",children:"Authorization"})}),e.jsxs("div",{className:"space-y-4",children:[e.jsx(p,{placeholder:"Username",autoFocus:!0,value:t,onChange:w,errorMessage:j}),e.jsx(p,{placeholder:"Password",type:x?"password":"text",value:u,onChange:P,errorMessage:v}),e.jsxs("div",{className:"flex justify-between",children:[e.jsx(T,{color:"dark",value:x,label:"Hide password",onChange:s=>N(s.target.checked)}),e.jsx("a",{className:"link-blue",href:"/reset-password",children:"Forgot password?"})]})]}),e.jsx("div",{children:e.jsx("button",{className:"btn-dark w-full text-xl",onClick:y,children:"Log in"})})]})})},M=()=>e.jsxs(L,{children:[e.jsx("a",{className:"flex h-full items-center font-logo text-3xl",href:"/",children:e.jsx(k,{})}),e.jsx("div",{className:"h-full flex justify-end items-center",children:e.jsx("a",{className:"btn-dark-outline",href:"/signup",children:"Create ID"})})]}),B=()=>e.jsx(e.Fragment,{children:e.jsxs("div",{className:"flex flex-col h-full",children:[e.jsx(M,{}),e.jsx("main",{className:"flex flex-col justify-center items-center grow",children:e.jsx(F,{})})]})}),D=()=>e.jsx(e.Fragment,{children:e.jsx(A,{children:e.jsx(B,{})})}),f=document.getElementById("p-login");f&&S(f).render(e.jsx(D,{}));
