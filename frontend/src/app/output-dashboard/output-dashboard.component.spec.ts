import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OutputDashboardComponent } from './output-dashboard.component';

describe('OutputDashboardComponent', () => {
  let component: OutputDashboardComponent;
  let fixture: ComponentFixture<OutputDashboardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OutputDashboardComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OutputDashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
