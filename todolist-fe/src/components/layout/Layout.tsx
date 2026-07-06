import React from "react";
import Header from "./Header";

interface LayoutProps {
  children: React.ReactNode;
}

export default function xLayout({ children }: LayoutProps) {
  return (
    <div className="flex flex-col md:flex-row min-h-screen bg-slate-50">
      <div className="flex-1 flex flex-col min-w-0">
        <Header />
        <main className="flex-1 px-6 py-8 md:px-10 lg:px-12 max-w-7xl mx-auto w-full">
          {children}
        </main>
      </div>
    </div>
  );
}
