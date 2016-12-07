import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';

import { AppComponent } from './app.component';
import { JobInfoComponent } from './jobinfo.component';
import { JobComponent } from './job.component';
import { JobInfoService } from './jobinfo.service';

@NgModule( {
    imports: [BrowserModule, FormsModule, HttpModule],
    declarations: [AppComponent, JobInfoComponent, JobComponent],
    providers: [JobInfoService],
    bootstrap: [AppComponent]
})
export class AppModule { }
